package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class TokenCountingMapperFactory extends AMapReduceTracer{
	private static TokenCountingMapperInterface<String, Integer> mapper;
	
	public static TokenCountingMapperInterface<String, Integer> getMapper() {
		if (mapper == null) {
			mapper = new TokenCountingMapper();
			traceSingletonChange(TokenCountingMapperFactory.class, mapper);
		}
		return mapper;
	}
	
	public static void setMapper(final TokenCountingMapperInterface<String, Integer> newMapper) {
		mapper = newMapper;
		traceSingletonChange(TokenCountingMapperFactory.class, mapper);
	}
}
