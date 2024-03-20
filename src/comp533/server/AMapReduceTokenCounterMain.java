package comp533.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import comp533.factories.AMapperFactory;
import comp533.factories.AReducerFactory;
import comp533.mapper.ATokenMapper;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.reduce.ATokenReducer;

public class AMapReduceTokenCounterMain {
	static final int PORT_NUMBER = 1099;

	public static void main(final String[] args) throws RemoteException, AlreadyBoundException, NotBoundException {
		final MapReduceModel model = new AMapReduceModel();
		final Registry registry = LocateRegistry.createRegistry(PORT_NUMBER);
		UnicastRemoteObject.exportObject(model, PORT_NUMBER);
		registry.rebind(model.toString(), model);

		AMapperFactory.setMapper(new ATokenMapper());
		AReducerFactory.setReducer(new ATokenReducer());

		new Thread(new AMapReduceTokenCounterServer(model)).run();
		System.exit(0);
	}
}
