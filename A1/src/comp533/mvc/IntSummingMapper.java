package comp533.mvc;

import java.util.ArrayList;
import java.util.List;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class IntSummingMapper extends AMapReduceTracer implements IntSummingMapperInterface<String, Integer>{
	final String resultKey = "ResultKey";
	
	@Override
	public List<KeyValueInterface<String, Integer>> map(final List<String> listOfToken) {
		final List<KeyValueInterface<String, Integer>> keyValList = new ArrayList<KeyValueInterface<String, Integer>> ();
		for (String a : listOfToken) {
			final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
			keyVal.setKey(resultKey);
			keyVal.setValue(Integer.parseInt(a));
			keyValList.add(keyVal);
		}
		traceMap(listOfToken ,keyValList);
		return keyValList;
	}
	
	@Override
	public String toString() {
		return INT_SUMMING_MAPPER; 
	}

}
