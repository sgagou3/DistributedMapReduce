package comp533.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import comp533.mvc.model.MapReduceModel;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AClientMain extends AMapReduceTracer {
	public static void main(final String[] args) throws RemoteException, NotBoundException {
		final ClientTokenCounter aClient = new AClientTokenCounter();
		final Registry aRegistry = LocateRegistry.getRegistry(1099);
		final MapReduceModel aModel = (MapReduceModel) aRegistry.lookup(MODEL);
		
		UnicastRemoteObject.exportObject(aClient, 0);
		
		aModel.register(aClient);
		aClient.pause();
		traceExit(AClientMain.class);
		System.exit(0);
	}
}
