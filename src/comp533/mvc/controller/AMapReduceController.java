package comp533.mvc.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp533.mvc.model.MapReduceModel;
import comp533.slave.AMapReduceSlave;
import comp533.slave.MapReduceSlave;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceController extends AMapReduceTracer implements MapReduceController {
	MapReduceModel model;
	List<MapReduceSlave> slaveList = new ArrayList<MapReduceSlave>();

	public AMapReduceController(final MapReduceModel aModel) {
		model = aModel;
	}

	@Override
	public void processInput() {
		final Scanner scanner = new Scanner(System.in);
		String aNewInputString = null;

		traceThreadPrompt();
		aNewInputString = scanner.nextLine();
		try {
			relayNumThreadString(aNewInputString);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		while (!QUIT.equals(aNewInputString)) {
			traceNumbersPrompt();
			aNewInputString = scanner.nextLine();
			try {
				relayInputString(aNewInputString);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		scanner.close();
		try {
			model.quit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		traceQuit();
	}

	private void relayInputString(final String aNewInputString) throws RemoteException {
		if (!QUIT.equals(aNewInputString)) {
			for (int aCounter = 0; aCounter < model.getNumThreads(); aCounter++) {
				final MapReduceSlave aSlave = (MapReduceSlave) slaveList.get(aCounter);
				aSlave.notifySlave();
			}
			model.setInputString(aNewInputString);
		}
	}

	private void relayNumThreadString(final String aNewNumThreadsString) throws RemoteException {
		if (!QUIT.equals(aNewNumThreadsString)) {
			final int anInputThreadCount = Integer.parseInt(aNewNumThreadsString);
			for (int aCounter = 0; aCounter < anInputThreadCount; aCounter++) {
				final MapReduceSlave aSlave = new AMapReduceSlave(model, aCounter);
				aSlave.notifySlave();
				slaveList.add(aSlave);
				traceRegister(aSlave);
			}
			model.setRunnableList(slaveList);
			model.setNumThreads(anInputThreadCount);
		}
	}

	@Override
	public String toString() {
		return CONTROLLER;
	}

	@Override
	public Runnable getSlave(final int anIndex) {
		if (anIndex >= 0 && anIndex < slaveList.size()) {
			return slaveList.get(anIndex);
		}
		throw new RuntimeException();
	}
}
