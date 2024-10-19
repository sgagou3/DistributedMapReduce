package comp533.mvc.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.AMapReduceBarrier;
import comp533.barrier.MapReduceBarrier;
import comp533.client.ClientTokenCounter;
import comp533.factories.AMapperFactory;
import comp533.joiner.AMapReduceJoiner;
import comp533.joiner.MapReduceJoiner;
import comp533.mapper.TokenMapper;
import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import comp533.slave.MapReduceSlave;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceModel extends AMapReduceTracer implements MapReduceModel {
	// Mappings
	Map mapReduceResult;
	Map<Integer, ClientTokenCounter> slaveClientMapping = new HashMap<Integer, ClientTokenCounter>();

	// Stacks
	Stack<MapReduceSlave> slaveStack = new Stack<MapReduceSlave>();
	Stack<ClientTokenCounter> clientStack = new Stack<ClientTokenCounter>();

	// Lists
	List<Thread> threadList;
	List<MapReduceSlave> slaveList;

	// Reduction data structures
	BlockingQueue<MapEntry> blockingQueue = new ArrayBlockingQueue<MapEntry>(BUFFER_SIZE);
	List<LinkedList<MapEntry>> reductionList = new ArrayList<LinkedList<MapEntry>>();

	// Blockers
	MapReduceJoiner joiner;
	MapReduceBarrier barrier;

	// Others
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	String inputString;
	int threadCount;

	@Override
	public String getInputString() {
		return inputString;
	}

	@Override
	public String toString() {
		return MODEL;
	}
	
	@Override
	public void setInputString(final String aNewInputString) {
		final String anOldInputString = inputString;

		inputString = aNewInputString;

		final PropertyChangeEvent anInputPropertyChangeEvent = new PropertyChangeEvent(this, "InputString",
				anOldInputString, aNewInputString);

		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEvent);

		final String[] aSplitInputStringArray = inputString.split(" ");
		final List<String> aSplitInputArrayList = Arrays.asList(aSplitInputStringArray);

		final TokenMapper<String, Integer> mapper = (TokenMapper<String, Integer>) AMapperFactory.getMapper();
		final List<MapEntry<String, Integer>> aMappingResult = mapper.map(aSplitInputArrayList);

		final int aSplitInputArrayListLength = aMappingResult.size();

		for (int aCounter = 0; aCounter < aSplitInputArrayListLength; aCounter++) {
			try {
				traceEnqueueRequest(aMappingResult.get(aCounter));
				blockingQueue.put(aMappingResult.get(aCounter));
				traceEnqueue(aMappingResult.get(aCounter));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		addBlockersToQueue();

		joiner.join();

		traceJoinerRelease(this, threadCount, threadCount);

		final Map<String, Integer> aReducingResult = new HashMap<String, Integer>();

		completeReduction(aReducingResult);

		final PropertyChangeEvent aReductionPropertyChangeEvent = new PropertyChangeEvent(this, "Result", null,
				aReducingResult.toString());
		propertyChangeSupport.firePropertyChange(aReductionPropertyChangeEvent);

		mapReduceResult = aReducingResult;

		clearReductionQueueList();
	}

	private void addBlockersToQueue() {
		for (int aCounter = 0; aCounter < threadCount; aCounter++) {
			try {
				traceEnqueueRequest(new AMapEntry<String, Integer>(null, null));
				blockingQueue.put(new AMapEntry<String, Integer>(null, null));
				traceEnqueue(new AMapEntry<String, Integer>(null, null));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void completeReduction(final Map aReducingResult) {
		for (final LinkedList<MapEntry> aNextReductionList : reductionList) {
			placeReductionResult(aNextReductionList, aReducingResult);
			traceAddedToMap(aReducingResult, aNextReductionList);
		}
	}

	private void placeReductionResult(final LinkedList<MapEntry> aNextReductionList,
			final Map<String, Object> aReducingResult) {
		for (MapEntry<String, Object> aNextReduction : aNextReductionList) {
			aReducingResult.put(aNextReduction.getKey(), aNextReduction.getValue());
		}
	}

	private void clearReductionQueueList() {
		for (LinkedList<MapEntry> aReductionQueue : reductionList) {
			aReductionQueue.clear();
		}
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener aNewListener) {
		propertyChangeSupport.addPropertyChangeListener(aNewListener);
	}

	@Override
	public Map<String, Integer> getResult() {
		return mapReduceResult;
	}

	@Override
	public int getNumThreads() {
		return threadCount;
	}

	@Override
	public BlockingQueue<MapEntry> getKeyValueQueue() {
		return blockingQueue;
	}

	@Override
	public List<LinkedList<MapEntry>> getReductionQueueList() {
		return reductionList;
	}

	@Override
	public MapReduceJoiner getJoiner() {
		return joiner;
	}

	@Override
	public MapReduceBarrier getBarrier() {
		return barrier;
	}

	@Override
	public void setRunnableList(final List<MapReduceSlave> aSlaveList) {
		slaveList = aSlaveList;
	}

	@Override
	public void register(final ClientTokenCounter aClient) {
		traceRegister(aClient);
		clientStack.push(aClient);
		match();
	}

	@Override
	public void match() {
		if (!slaveStack.isEmpty() && !clientStack.isEmpty()) {
			final ClientTokenCounter aCounter = clientStack.pop();
			final MapReduceSlave aSlave = slaveStack.pop();
			slaveClientMapping.put(aSlave.getSlaveNumber(), aCounter);
			traceClientAssignment(aCounter);
			match();
		}
	}

	@Override
	public Map<Integer, ClientTokenCounter> getSlaveClientMapping() {
		return slaveClientMapping;
	}

	@Override
	public void setNumThreads(final int aNewNumThreadCount) throws RemoteException {
		final List<Thread> aNewThreadList = new ArrayList<Thread>();
		final PropertyChangeEvent anInputPropertyChangeEvent = new PropertyChangeEvent(this, "NumThreads", threadCount,
				aNewNumThreadCount);
		threadCount = aNewNumThreadCount;
		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEvent);

		joiner = new AMapReduceJoiner(threadCount);
		traceJoinerCreated(this, threadCount);
		barrier = new AMapReduceBarrier(threadCount);
		traceBarrierCreated(this, threadCount);
		
		for (int aCounter = 0; aCounter < threadCount; aCounter++) {
			final MapReduceSlave aSlave = slaveList.get(aCounter);
			final Thread aThread = new Thread(aSlave);
			aThread.setName("Slave" + aCounter);
			aNewThreadList.add(aThread);
			slaveStack.push(aSlave);
			reductionList.add(new LinkedList<MapEntry>());
			aThread.start();
		}

		match();

		final PropertyChangeEvent anInputPropertyChangeEventForThreadList = new PropertyChangeEvent(this, "Threads",
				threadList, aNewThreadList);
		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEventForThreadList);
		threadList = aNewThreadList;
	}

	@Override
	public void quit() throws RemoteException {
		for (Entry<Integer, ClientTokenCounter> anEntry : slaveClientMapping.entrySet()) {
			try {
				anEntry.getValue().quit();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		for (Thread aThread : threadList) {
			aThread.interrupt();
		}

		traceQuit();
		traceNotify();
		System.exit(0);
	}
}
