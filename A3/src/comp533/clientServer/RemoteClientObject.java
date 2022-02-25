package comp533.clientServer;


import java.util.List;
import java.util.Map;

import comp533.mvc.KeyValueInterface;
import comp533.mvc.ReducerFactory;
import comp533.mvc.ReducerInterface;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class RemoteClientObject extends AMapReduceTracer implements RemoteClientInterface{

	@Override
	public Map<String, Integer> reduce(List<KeyValueInterface<String, Integer>> aList) {
		final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
		traceRemoteList(aList);
		Map<String, Integer> result = reducer.reduce(aList);
		traceRemoteResult(result);
		return result;
	}



}
