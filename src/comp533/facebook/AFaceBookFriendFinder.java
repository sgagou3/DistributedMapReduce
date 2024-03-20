package comp533.facebook;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comp533.mvc.controller.AMapReduceController;
import comp533.mvc.controller.MapReduceController;
import comp533.mvc.model.MapReduceModel;
import comp533.mvc.view.AMapReduceView;
import comp533.mvc.view.MapReduceView;

public class AFaceBookFriendFinder implements FaceBookFriendFinder {
	MapReduceModel model;
	MapReduceView view = new AMapReduceView();
	MapReduceController controller;

	public AFaceBookFriendFinder(final MapReduceModel aModel)
			throws RemoteException, AlreadyBoundException, NotBoundException {
		model = aModel;
		model.addPropertyChangeListener(view);
		controller = new AMapReduceController(model);
	}

	@Override
	public void processInput() {
		controller.processInput();
	}

}
