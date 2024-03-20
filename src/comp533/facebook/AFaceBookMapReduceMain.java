package comp533.facebook;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import comp533.factories.AMapperFactory;
import comp533.factories.AReducerFactory;
import comp533.mapper.AFaceBookFriendMapper;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.reduce.AFaceBookFriendReducer;

public class AFaceBookMapReduceMain {
	static final int PORT_NUMBER = 1099;
	
	// Distributed Facebook Friend Map Reduce with 4 threads and 2 clients
	// Input: a,b,c b,a,c c,a,b,d d,c

	public static void main(final String[] args) throws RemoteException, AlreadyBoundException, NotBoundException {
		final MapReduceModel model = new AMapReduceModel();
		final Registry registry = LocateRegistry.createRegistry(PORT_NUMBER);
		UnicastRemoteObject.exportObject(model, PORT_NUMBER);
		registry.bind(model.toString(), model);

		AMapperFactory.setMapper(new AFaceBookFriendMapper());
		AReducerFactory.setReducer(new AFaceBookFriendReducer());

		new Thread(new AFaceBookMapReduceServer(model)).run();
		System.exit(0);
	}
}
