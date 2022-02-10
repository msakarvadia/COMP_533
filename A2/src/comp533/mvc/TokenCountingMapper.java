package comp533.mvc;

import java.util.List;
import java.util.ArrayList;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class TokenCountingMapper extends AMapReduceTracer implements TokenCountingMapperInterface<String, Integer>{
	final static int ONE = 1;
	
	@Override
	public KeyValueInterface<String, Integer> map(final java.lang.String aString) {
		final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
		keyVal.setKey(aString);
		keyVal.setValue(ONE);
		
		traceMap(aString ,keyVal);
		return keyVal;
	}
	
	@Override
	public String toString() {
		return TOKEN_COUNTING_MAPPER; 
	}

}
