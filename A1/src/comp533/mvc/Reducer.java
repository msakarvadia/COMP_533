package comp533.mvc;

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
		// TODO Auto-generated method stub
		return null;
	}
}
