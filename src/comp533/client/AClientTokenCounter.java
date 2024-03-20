package comp533.client;

import java.util.List;
import java.util.Map;

import comp533.factories.AReducerFactory;
import comp533.reduce.MapEntry;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

@SuppressWarnings("serial")
public class AClientTokenCounter extends AMapReduceTracer implements ClientTokenCounter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map reduce(final List<MapEntry> aMapEntryList) {
		traceRemoteList(aMapEntryList);
		final TokenReducer aReducer = AReducerFactory.getReducer();
		final Map aResult = aReducer.reduce(aMapEntryList);
		traceRemoteResult(aResult);
		return aResult;
	}

	@Override
	public void quit() {
		traceQuit();
		synchronizedNotify();
	}

	@Override
	public void pause() {
		try {
			synchronizedWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return CLIENT;
	}
}
