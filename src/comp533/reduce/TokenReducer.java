package comp533.reduce;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TokenReducer<K, V> extends Serializable{
	Map<K, V> reduce(List<MapEntry<K, V>> aMapEntryList);
}
