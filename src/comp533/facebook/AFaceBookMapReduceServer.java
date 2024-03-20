package comp533.facebook;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comp533.mvc.model.MapReduceModel;
import comp533.server.MapReduceServer;

public class AFaceBookMapReduceServer implements MapReduceServer {
	FaceBookFriendFinder finder;

	public AFaceBookMapReduceServer(final MapReduceModel aModel)
			throws RemoteException, AlreadyBoundException, NotBoundException {
		finder = new AFaceBookFriendFinder(aModel);
	}

	@Override
	public void run() {
		finder.processInput();
		System.exit(0);
	}
}
