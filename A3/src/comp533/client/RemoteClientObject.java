package comp533.client;


import java.util.List;
import java.util.Map;

import comp533.mvc.KeyValueInterface;
import comp533.mvc.ReducerFactory;
import comp533.mvc.ReducerInterface;

public class RemoteClientObject implements RemoteClientInterface{

	@Override
	public Map<String, Integer> reduce(List<KeyValueInterface<String, Integer>> aList) {
		final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
		Map<String, Integer> result = reducer.reduce(aList);
		return result;
	}

}
