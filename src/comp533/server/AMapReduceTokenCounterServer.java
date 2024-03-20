package comp533.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comp533.base.ATokenCounter;
import comp533.base.TokenCounter;
import comp533.mvc.model.MapReduceModel;

public class AMapReduceTokenCounterServer implements MapReduceServer {
	TokenCounter counter;

	public AMapReduceTokenCounterServer(final MapReduceModel aModel)
			throws RemoteException, AlreadyBoundException, NotBoundException {
		counter = new ATokenCounter(aModel);
	}

	@Override
	public void run() {
		counter.processInput();
		System.exit(0);
	}
}
