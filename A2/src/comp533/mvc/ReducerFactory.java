package comp533.mvc;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class ReducerFactory extends AMapReduceTracer{
	private static ReducerInterface<String, Integer> reducer;
	
	static {
		reducer = new Reducer();
	}
	
	public static ReducerInterface<String, Integer> getReducer() {
		return reducer;
	}
	
	public static void setReducer(final ReducerInterface<String, Integer> newReducer) {
		reducer = newReducer;
		traceSingletonChange(ReducerFactory.class, reducer);
	}
}
