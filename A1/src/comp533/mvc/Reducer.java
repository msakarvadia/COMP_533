package comp533.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Reducer extends AMapReduceTracer implements ReducerInterface<String,Integer> {

	@Override
	public String toString() {
		return REDUCER;
	}

	@Override
	public Map<String, Integer> reduce(List<KeyValueInterface<String, Integer>> aList) {
		Map<String, Integer> Result =new HashMap<String, Integer>();
		for (KeyValueInterface<String, Integer> a : aList) {
			if (!Result.containsKey(a.getKey())) {
				//Result.put(a.getKey(),1);
				Result.put(a.getKey(),a.getValue());
			}
			else {
				Result.put(a.getKey(), Result.get(a.getKey()) + a.getValue());
			}
		}
		traceReduce(aList, Result);
		return Result;
	}
}
