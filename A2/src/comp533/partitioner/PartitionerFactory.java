package comp533.partitioner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class PartitionerFactory extends AMapReduceTracer{
	private static PartitionerInterface<String, Integer> partitioner ;
	
	static {
		partitioner = new Partitioner<String, Integer>();
	}
	
	public static PartitionerInterface<String, Integer> getPartitioner() {
		
		return partitioner;
	}
	
	public static void setPartitioner(final PartitionerInterface<String, Integer> newPartitioner) {
		partitioner = newPartitioner;
		traceSingletonChange(PartitionerFactory.class, partitioner);
	}
}
