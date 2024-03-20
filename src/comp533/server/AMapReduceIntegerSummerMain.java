package comp533.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import comp533.factories.AMapperFactory;
import comp533.factories.AReducerFactory;
import comp533.mapper.AnIntegerSummingMapper;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.reduce.AnIntegerSummingReducer;

public class AMapReduceIntegerSummerMain {
	static final int PORT_NUMBER = 1099;

	public static void main(final String[] args) throws RemoteException, AlreadyBoundException, NotBoundException {
		final MapReduceModel model = new AMapReduceModel();
		final Registry registry = LocateRegistry.createRegistry(PORT_NUMBER);
		UnicastRemoteObject.exportObject(model, PORT_NUMBER);
		registry.bind(model.toString(), model);
		
		AMapperFactory.setMapper(new AnIntegerSummingMapper());
		AReducerFactory.setReducer(new AnIntegerSummingReducer());
		
		new Thread(new AMapReduceIntegerSummerServer(model)).run();
		System.exit(0);
	}
}
