package comp533.mvc;

import comp533.barrier.Barrier;
import comp533.clientServer.RemoteClientInterface;
import comp533.clientServer.RemoteClientObject;
import comp533.clientServer.RemoteClientProcess;
import comp533.clientServer.ServerTokenCounter;
import comp533.joiner.Joiner;
import comp533.partitioner.Partitioner;
import comp533.partitioner.PartitionerFactory;
import comp533.salve.Slave;

//import gradingTools.comp533s21.assignment1.interfaces.MapReduceConfiguration;

public class MapReduceConfiguration implements MapReduceConfigurationInterface {

	@Override
	public Object getBarrier(final int arg0) {
		return new Barrier(arg0);
	}

	@Override
	public Class<?> getBarrierClass() {
		return Barrier.class;
	}

	@Override
	public Class<?> getClientTokenCounter() {
		return RemoteClientProcess.class;
	}

	@Override
	public Class<?> getControllerClass() {
		return Controller.class;
	}

	@Override
	public Object getIntSummingMapper() {
		return IntSummingMapperFactory.getMapper();
		
	}

	@Override
	public Class<?> getIntSummingMapperClass() {
		return IntSummingMapper.class;
	}

	@Override
	public Object getJoiner(final int arg0) {
		return new Joiner(arg0);
	}

	@Override
	public Class<?> getJoinerClass() {
		return Joiner.class;
	}

	@Override
	public Class<?> getKeyValueClass() {
		return KeyValue.class;
	}

	@Override
	public Class<?> getMapperFactory() {
		return TokenCountingMapperFactory.class;
	}

	@Override
	public Class<?> getModelClass() {
		return Model.class;
	}

	@Override
	public Object getPartitioner() {
		return PartitionerFactory.getPartitioner();
	}

	@Override
	public Class<?> getPartitionerClass() {
		return Partitioner.class;
	}

	@Override
	public Class<?> getPartitionerFactory() {
		return PartitionerFactory.class;
	}

	@Override
	public Object getReducer() {
		return ReducerFactory.getReducer();
	}

	@Override
	public Class<?> getReducerClass() {
		return Reducer.class;
	}

	@Override
	public Class<?> getReducerFactory() {
		return ReducerFactory.class;
	}

	@Override
	public Class<?> getRemoteClientFacebookMapReduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRemoteClientObjectClass() {
		
		return RemoteClientObject.class;
	}

	@Override
	public Class<?> getRemoteClientObjectInterface() {
		
		return RemoteClientInterface.class;
	}

	@Override
	public Class<?> getRemoteModelInterface() {
		
		return RemoteModelInterface.class;
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
		return ServerTokenCounter.class;
	}

	@Override
	public Class<?> getSlaveClass() {
		return Slave.class;
	}

	@Override
	public Class<?> getStandAloneFacebookMapReduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getStandAloneIntegerSummer() {
		return StandAloneIntSumming.class;
	}

	@Override
	public Class<?> getStandAloneTokenCounter() {
		return ModelViewController.class;
		//return StandAloneTokenCounter.class;
	}

	@Override
	public Object getTokenCountingMapper() {
		return TokenCountingMapperFactory.getMapper();
	}

	@Override
	public Class getTokenCountingMapperClass() {
		return TokenCountingMapper.class;
	}

	@Override
	public Class<?> getViewClass() {
		return View.class;
	}

}
