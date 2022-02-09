package comp533.mvc;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class ReducerFactory extends AMapReduceTracer{
	private static ReducerInterface<String, Integer> reducer;
	
	public static ReducerInterface<String, Integer> getReducer() {
		if (reducer == null) {
			reducer = new Reducer();
		}
		return reducer;
	}
	
	public static void setReducer(final ReducerInterface<String, Integer> newReducer) {
		reducer = newReducer;
		traceSingletonChange(ReducerFactory.class, reducer);
	}
}
