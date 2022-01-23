package comp533.mvc;

public class MapReduceConfiguration implements gradingTools.comp533s21.assignment1.interfaces.MapReduceConfiguration {

	@Override
	public Object getBarrier(final int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getBarrierClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getClientTokenCounter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getControllerClass() {
		// TODO Auto-generated method stub
		return Controller.class;
	}

	@Override
	public Object getIntSummingMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getIntSummingMapperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getJoiner(final int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getJoinerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getKeyValueClass() {
		// TODO Auto-generated method stub
		return KeyValue.class;
	}

	@Override
	public Class<?> getMapperFactory() {
		// TODO Auto-generated method stub
		return TokenCountingMapperFactory.class;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return Model.class;
	}

	@Override
	public Object getPartitioner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getPartitionerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getPartitionerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getReducer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getReducerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getReducerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRemoteClientFacebookMapReduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRemoteClientObjectClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRemoteClientObjectInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRemoteModelInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getServerFacebookMapReduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getServerIntegerSummer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getServerTokenCounter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getSlaveClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getStandAloneFacebookMapReduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getStandAloneIntegerSummer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getStandAloneTokenCounter() {
		return ModelViewController.class;
		//return StandAloneTokenCounter.class;
	}

	@Override
	public Object getTokenCountingMapper() {
		// TODO Auto-generated method stub
		return TokenCountingMapperFactory.getMapper();
	}

	@Override
	public Class getTokenCountingMapperClass() {
		// TODO Auto-generated method stub
		//MAYBE RETURN NULL HERE
		return TokenCountingMapper.class;
	}

	@Override
	public Class<?> getViewClass() {
		// TODO Auto-generated method stub
		return View.class;
	}

}
