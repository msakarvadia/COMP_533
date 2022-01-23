package comp533.mvc;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class ReducerFactory extends AMapReduceTracer{
	private static ReducerInterface<String, Integer> Reducer;
	
	public static ReducerInterface<String, Integer> getReducer() {
		if (Reducer == null) {
			Reducer = new Reducer();
		}
		return Reducer;
	}
	
	public static void setReducer(final ReducerInterface<String, Integer> newReducer) {
		Reducer = newReducer;
		traceSingletonChange(ReducerFactory.class, Reducer);
	}
}
