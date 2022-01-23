package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class TokenCountingMapperFactory extends AMapReduceTracer{
	private static TokenCountingMapperInterface<String, Integer> Mapper;
	
	public static TokenCountingMapperInterface<String, Integer> getMapper() {
		if (Mapper == null) {
			Mapper = new TokenCountingMapper();
			traceSingletonChange(IntSummingMapperFactory.class, Mapper);
		}
		return Mapper;
	}
	
	public static void setMapper(final TokenCountingMapperInterface<String, Integer> newMapper) {
		Mapper = newMapper;
		traceSingletonChange(TokenCountingMapperFactory.class, Mapper);
	}
}
