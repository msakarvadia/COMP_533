package comp533.mvc;

import java.util.ArrayList;
import java.util.List;

public class Testing {
	public static void main(String[] args) {
		List<KeyValueInterface<String, Integer>> KeyValList = new ArrayList<KeyValueInterface<String, Integer>> ();
		KeyValueInterface<String, Integer> KeyVal = new KeyValue<String, Integer>();
		KeyVal.setKey("Hi");
		KeyVal.setValue(1);
		KeyValList.add(KeyVal);
		ReducerInterface<String, Integer> Reducer = ReducerFactory.getReducer();
		//ReducerInterface<String, Integer> Reducer = new Reducer();
		System.out.println(Reducer.reduce(KeyValList));
		
		
	}
}
