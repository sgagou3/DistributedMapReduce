package comp533.joiner;

import java.io.Serializable;

public interface MapReduceJoiner extends Serializable{
	void finished();
	void join();
}
