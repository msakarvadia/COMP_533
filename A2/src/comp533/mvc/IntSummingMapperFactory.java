package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class IntSummingMapperFactory extends AMapReduceTracer{
	private static IntSummingMapperInterface<String, Integer> mapper;
	
	static {
		mapper = new IntSummingMapper();
	}
	
	public static IntSummingMapperInterface<String, Integer> getMapper() {
		
		return mapper;
	}
	
	public static void setMapper(final IntSummingMapperInterface<String, Integer> newMapper) {
		mapper = newMapper;
		traceSingletonChange(IntSummingMapperFactory.class, mapper);
	}

}
