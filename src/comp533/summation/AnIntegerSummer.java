package comp533.summation;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comp533.mvc.controller.AMapReduceController;
import comp533.mvc.controller.MapReduceController;
import comp533.mvc.model.MapReduceModel;
import comp533.mvc.view.AMapReduceView;
import comp533.mvc.view.MapReduceView;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

@SuppressWarnings("serial")
public class AnIntegerSummer extends AMapReduceTracer implements IntegerSummer {
	MapReduceModel model;
	MapReduceController controller;
	MapReduceView view = new AMapReduceView();

	public AnIntegerSummer(final MapReduceModel aModel)
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
