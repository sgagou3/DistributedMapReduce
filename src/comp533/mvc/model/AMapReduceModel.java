package comp533.mvc.model;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import comp533.reduce.MapEntry;
import comp533.slave.MapReduceSlave;

public class AMapReduceModel extends AnAllocatingMapReduceModel implements Serializable {
	List<Thread> threadList;

	@Override
	public void setNumThreads(final int aNewNumThreadCount) throws RemoteException {
		final List<Thread> aNewThreadList = new ArrayList<Thread>();
		super.setNumThreads(aNewNumThreadCount);

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
	public void quit() {
		try {
			super.quit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		for (Thread aThread : threadList) {
			aThread.interrupt();
		}

		traceQuit();
		traceNotify();
		System.exit(0);
	}
}
