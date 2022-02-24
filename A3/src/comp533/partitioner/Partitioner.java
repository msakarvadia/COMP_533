package comp533.partitioner;


import gradingTools.comp533s19.assignment0.AMapReduceTracer;


//TODO MIGHT NEED TO FIX THE GENERIC TYPE OF THE VALUE:
public class Partitioner<K extends String, V> extends AMapReduceTracer implements PartitionerInterface<K , V> {
	final int one = 1;
	final char firstLetter = 'a';
	final char lastLetter = 'z';
	final int firstCharIndex = 0;
	
	
	@Override
	public int getPartition(final K key, final V value, final int numPartitions) {
		int aPartitionNum = 0;
		
		final char firstChar = key.charAt(firstCharIndex);
		final char firstCharLower = Character.toLowerCase(firstChar);
		
		
	
		if (!Character.isLetter(firstChar)){
			aPartitionNum=0;
			tracePartitionAssigned(key, value, aPartitionNum, numPartitions);
			return aPartitionNum;
		}
		
		final double maxPartitionSize = Math.ceil((lastLetter-firstLetter+one)/(double)numPartitions);
		aPartitionNum = (int) Math.floor((firstCharLower -firstLetter+one)/(double)maxPartitionSize);
		
		tracePartitionAssigned(key, value, aPartitionNum, numPartitions);
		return aPartitionNum;
	}
	
	@Override
	public String toString() {
		return PARTITIONER;
	}

}
