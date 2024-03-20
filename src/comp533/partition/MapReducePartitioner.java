package comp533.partition;

import java.io.Serializable;

public interface MapReducePartitioner<K, V> extends Serializable {
	int getPartition(K key, Object value, int numberOfPartitions);
}
