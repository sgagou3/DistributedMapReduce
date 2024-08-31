package comp533.mvc.model;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.MapReduceBarrier;
import comp533.client.ClientTokenCounter;
import comp533.joiner.MapReduceJoiner;
import comp533.reduce.MapEntry;
import comp533.slave.MapReduceSlave;

public interface MapReduceModel extends Serializable, Remote {

	String getInputString() throws RemoteException;

	void setInputString(String aNewInputString) throws RemoteException;

	void addPropertyChangeListener(PropertyChangeListener aNewListener) throws RemoteException;

	Map<String, Integer> getResult() throws RemoteException;

	void setNumThreads(int aNewNumThreadCount) throws RemoteException;

	int getNumThreads() throws RemoteException;

	void setRunnableList(final List<MapReduceSlave> aSlaveList) throws RemoteException;

	BlockingQueue<MapEntry> getKeyValueQueue() throws RemoteException;

	List<LinkedList<MapEntry>> getReductionQueueList() throws RemoteException;

	MapReduceJoiner getJoiner() throws RemoteException;

	MapReduceBarrier getBarrier() throws RemoteException;

	void quit() throws RemoteException;

	void register(ClientTokenCounter aClient) throws RemoteException;

	void match() throws RemoteException;

	Map<Integer, ClientTokenCounter> getSlaveClientMapping() throws RemoteException;
}
