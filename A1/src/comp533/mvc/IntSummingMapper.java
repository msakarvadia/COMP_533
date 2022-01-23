package comp533.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class IntSummingMapper extends AMapReduceTracer implements IntSummingMapperInterface<String, Integer>{

	@Override
	public List<KeyValueInterface<String, Integer>> map(String aStrings) {
		final List<String> ListOfToken = Arrays.asList(aStrings.split(" "));
		List<KeyValueInterface<String, Integer>> KeyValList = new ArrayList<KeyValueInterface<String, Integer>> ();
		for (String a : ListOfToken) {
			KeyValueInterface<String, Integer> KeyVal = new KeyValue<String, Integer>();
			KeyVal.setKey("ResultKey");
			KeyVal.setValue(Integer.parseInt(a));
			KeyValList.add(KeyVal);
		}
		traceMap(aStrings ,KeyValList);
		return KeyValList;
	}
	
	@Override
	public String toString() {
		return INT_SUMMING_MAPPER; 
	}

}
