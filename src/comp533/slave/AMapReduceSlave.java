package comp533.slave;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.MapReduceBarrier;
import comp533.client.ClientTokenCounter;
import comp533.factories.APartitionerFactory;
import comp533.factories.AReducerFactory;
import comp533.joiner.MapReduceJoiner;
import comp533.mvc.model.MapReduceModel;
import comp533.partition.MapReducePartitioner;
import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceSlave extends AMapReduceTracer implements MapReduceSlave {
	MapReduceModel model;
	MapReduceBarrier barrier;
	MapReduceJoiner joiner;

	int slaveNumber;

	MapReducePartitioner partitioner;
	TokenReducer reducer;
	List<LinkedList<MapEntry>> aReductionQueueList;

	public AMapReduceSlave(final MapReduceModel aModel, final int aSlaveNumber) {
		model = aModel;
		slaveNumber = aSlaveNumber;
		partitioner = APartitionerFactory.getPartitioner();
		reducer = AReducerFactory.getReducer();
		try {
			aReductionQueueList = model.getReductionQueueList();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private List<MapEntry> getInput() throws RemoteException {
		final List<MapEntry> aConsumedList = new ArrayList<MapEntry>();
		final BlockingQueue<MapEntry> aBlockingQueue = model.getKeyValueQueue();
		MapEntry<String, Integer> aNextElement;
		try {
			while (true) {
				aNextElement = aBlockingQueue.take();
				if (aNextElement == null) {
					traceDequeueRequest(aBlockingQueue);
					continue;
				} else if (aNextElement.getKey() != null && aNextElement.getValue() != null) {
					traceDequeueRequest(aBlockingQueue);
					aConsumedList.add(aNextElement);
					traceDequeue(aNextElement);
				} else {
					traceDequeueRequest(aBlockingQueue);
					traceDequeue(aNextElement);
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return aConsumedList;
	}

	private void doFirstReduction(final Map<Object, Object> aReducedMap) throws RemoteException {
		synchronized (aReductionQueueList) {
			for (final Map.Entry aMapEntry : aReducedMap.entrySet()) {
				final int aPartitionIndex = partitioner.getPartition(aMapEntry.getKey(), aMapEntry.getValue(),
						model.getNumThreads());
				tracePartitionAssigned(aMapEntry.getKey(), aMapEntry.getValue(), aPartitionIndex,
						model.getNumThreads());
				aReductionQueueList.get(aPartitionIndex).add(new AMapEntry(aMapEntry.getKey(), aMapEntry.getValue()));
			}
		}
	}

	private void doSecondReduction() throws RemoteException {
		synchronized (aReductionQueueList) {
			final LinkedList<MapEntry> aFinalReductionList = aReductionQueueList.get(slaveNumber);
			traceSplitAfterBarrier(slaveNumber, aReductionQueueList.get(slaveNumber));

			final Map<Integer, ClientTokenCounter> aSlaveClientMapping = model.getSlaveClientMapping();

			Map<String, Integer> aFinalReductionMap = null;

			if (!aSlaveClientMapping.containsKey(slaveNumber)) {
				aFinalReductionMap = reducer.reduce(aFinalReductionList);
			} else {
				final ClientTokenCounter aCounter = aSlaveClientMapping.get(slaveNumber);
				traceRemoteList(aFinalReductionList);
				aFinalReductionMap = aCounter.reduce(aFinalReductionList);
				traceRemoteResult(aFinalReductionMap);
			}

			aReductionQueueList.get(slaveNumber).clear();
			for (final Map.Entry<String, Integer> aMapEntry : aFinalReductionMap.entrySet()) {
				aReductionQueueList.get(slaveNumber)
						.add(new AMapEntry(aMapEntry.getKey(), aMapEntry.getValue()));
			}
		}
	}

	@Override
	public void run() {
		try {
			barrier = model.getBarrier();
			joiner = model.getJoiner();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		while (!Thread.currentThread().isInterrupted()) {
			List<MapEntry> aConsumedList = null;
			try {
				aConsumedList = getInput();
				final Map<Integer, ClientTokenCounter> aSlaveClientMapping = model.getSlaveClientMapping();

				Map aReducedMap = null;

				if (!aSlaveClientMapping.containsKey(slaveNumber)) {
					aReducedMap = reducer.reduce(aConsumedList);
				} else {
					final ClientTokenCounter aCounter = aSlaveClientMapping.get(slaveNumber);
					traceRemoteList(aConsumedList);
					aReducedMap = aCounter.reduce(aConsumedList);
					traceRemoteResult(aReducedMap);
				}

				doFirstReduction(aReducedMap);
				barrier.barrier();
				doSecondReduction();
				joiner.finished();
				waitSlave();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void waitSlave() {
		try {
			traceWait();
			wait();
		} catch (InterruptedException e) {
			traceQuit();
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public synchronized void notifySlave() {
		traceNotify();
		notify();
	}

	@Override
	public String toString() {
		return SLAVE;
	}

	@Override
	public int getSlaveNumber() {
		return slaveNumber;
	}

}
