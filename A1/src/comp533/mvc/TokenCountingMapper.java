package comp533.mvc;

import java.util.List;
import java.util.*;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class TokenCountingMapper extends AMapReduceTracer implements TokenCountingMapperInterface<String, Integer>{

	@Override
	public List<KeyValueInterface<String, Integer>> map(List<java.lang.String> aStrings) {
		List<KeyValueInterface<String, Integer>> KeyValList = new ArrayList<KeyValueInterface<String, Integer>> ();
		for (String a : aStrings) {
			KeyValueInterface<String, Integer> KeyVal = new KeyValue<String, Integer>();
			KeyVal.setKey(a);
			KeyVal.setValue(1);
			KeyValList.add(KeyVal);
		}
		traceMap(aStrings ,KeyValList);
		return KeyValList;
	}
	
	@Override
	public String toString() {
		return TOKEN_COUNTING_MAPPER; 
	}

}
