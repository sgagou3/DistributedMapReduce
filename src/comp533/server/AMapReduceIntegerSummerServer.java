package comp533.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comp533.mvc.model.MapReduceModel;
import comp533.summation.AnIntegerSummer;
import comp533.summation.IntegerSummer;

public class AMapReduceIntegerSummerServer implements MapReduceServer {
	IntegerSummer summer;

	public AMapReduceIntegerSummerServer(final MapReduceModel aModel)
			throws RemoteException, AlreadyBoundException, NotBoundException {
		summer = new AnIntegerSummer(aModel);
	}

	@Override
	public void run() {
		summer.processInput();
		System.exit(0);
	}
}
