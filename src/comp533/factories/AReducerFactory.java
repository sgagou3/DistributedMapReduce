package comp533.factories;

import comp533.reduce.ATokenReducer;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AReducerFactory extends AMapReduceTracer {
	@SuppressWarnings("rawtypes")
	static TokenReducer reducer = new ATokenReducer();

	@SuppressWarnings("rawtypes")
	public static TokenReducer getReducer() {
		return reducer;
	}

	@SuppressWarnings("rawtypes")
	public static void setReducer(final TokenReducer aNewReducer) {
		traceSingletonChange(AReducerFactory.class, aNewReducer);
		reducer = aNewReducer;
	}
}
