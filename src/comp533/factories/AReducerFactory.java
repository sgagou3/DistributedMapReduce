package comp533.factories;

import comp533.reduce.ATokenReducer;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AReducerFactory extends AMapReduceTracer {
	static TokenReducer reducer = new ATokenReducer();

	public static TokenReducer getReducer() {
		return reducer;
	}

	public static void setReducer(final TokenReducer aNewReducer) {
		traceSingletonChange(AReducerFactory.class, aNewReducer);
		reducer = aNewReducer;
	}
}
