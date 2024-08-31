package comp533.client;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import comp533.reduce.MapEntry;

public interface ClientTokenCounter extends Serializable, Remote {
	Map<String, Integer> reduce(List<MapEntry> aMapEntryList) throws RemoteException;

	void pause() throws RemoteException;

	void quit() throws RemoteException;
}
