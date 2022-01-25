package comp533.mvc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Reducer extends AMapReduceTracer implements ReducerInterface<String,Integer> {

	@Override
	public String toString() {
		return REDUCER;
	}

	@Override
	public Map<String, Integer> reduce(final List<KeyValueInterface<String, Integer>> aList) {
		final Map<String, Integer> result =new HashMap<String, Integer>();
		final Iterator<KeyValueInterface<String, Integer>> iterator = aList.iterator();
		while(iterator.hasNext()) {
			final KeyValueInterface<String, Integer> val = iterator.next();
			if (!result.containsKey(val.getKey())) {
				//Result.put(a.getKey(),1);
				result.put(val.getKey(),val.getValue());
			}
			else {
				result.put(val.getKey(), result.get(val.getKey()) + val.getValue());
			}
		}
		traceReduce(aList, result);
		return result;
	}
}
