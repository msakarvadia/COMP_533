package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class IntSummingMapper extends AMapReduceTracer implements IntSummingMapperInterface<String, Integer>{
	final String resultKey = "ResultKey";
	
	@Override
	public KeyValueInterface<String, Integer> map(final String token) {
				
		final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
		keyVal.setKey(resultKey);
		keyVal.setValue(Integer.parseInt(token));
		
		traceMap(token ,keyVal);
		return keyVal;
	}
	
	@Override
	public String toString() {
		return INT_SUMMING_MAPPER; 
	}

}
