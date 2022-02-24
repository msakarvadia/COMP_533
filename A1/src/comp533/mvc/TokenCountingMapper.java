package comp533.mvc;

import java.util.List;
import java.util.ArrayList;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class TokenCountingMapper extends AMapReduceTracer implements TokenCountingMapperInterface<String, Integer>{
	final static int ONE = 1;
	
	@Override
	public List<KeyValueInterface<String, Integer>> map(final List<java.lang.String> aStrings) {
		final List<KeyValueInterface<String, Integer>> keyValList = new ArrayList<KeyValueInterface<String, Integer>> ();
		for (String a : aStrings) {
			final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
			keyVal.setKey(a);
			keyVal.setValue(ONE);
			keyValList.add(keyVal);
		}
		traceMap(aStrings ,keyValList);
		return keyValList;
	}
	
	@Override
	public String toString() {
		return TOKEN_COUNTING_MAPPER; 
	}

}
