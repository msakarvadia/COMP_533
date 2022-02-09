package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class IntSummingMapperFactory extends AMapReduceTracer{
	private static IntSummingMapperInterface<String, Integer> mapper;
	
	public static IntSummingMapperInterface<String, Integer> getMapper() {
		if (mapper == null) {
			mapper = new IntSummingMapper();
			traceSingletonChange(IntSummingMapperFactory.class, mapper);
		}
		return mapper;
	}
	
	public static void setMapper(final IntSummingMapperInterface<String, Integer> newMapper) {
		mapper = newMapper;
		traceSingletonChange(IntSummingMapperFactory.class, mapper);
	}

}
