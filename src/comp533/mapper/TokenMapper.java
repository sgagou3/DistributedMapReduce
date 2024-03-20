package comp533.mapper;

import java.io.Serializable;
import java.util.List;

import comp533.reduce.MapEntry;

public interface TokenMapper<K, V> extends Serializable{
	List<MapEntry<K, V>> map(List<String> aKeyList);
}
