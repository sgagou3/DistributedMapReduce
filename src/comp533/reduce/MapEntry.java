package comp533.reduce;

import java.io.Serializable;

public interface MapEntry<K, V> extends Serializable {
	K getKey();

	V getValue();

	void setValue(V aNewValue);
}
