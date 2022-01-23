package comp533.mvc;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class IntSummingMapperFactory extends AMapReduceTracer{
	private static IntSummingMapperInterface<String, Integer> Mapper;
	
	public static IntSummingMapperInterface<String, Integer> getMapper() {
		if (Mapper == null) {
			Mapper = new IntSummingMapper();
			traceSingletonChange(IntSummingMapperFactory.class, Mapper);
		}
		return Mapper;
	}
	
	public static void setMapper(final IntSummingMapperInterface<String, Integer> newMapper) {
		Mapper = newMapper;
		traceSingletonChange(IntSummingMapperFactory.class, Mapper);
	}

}
