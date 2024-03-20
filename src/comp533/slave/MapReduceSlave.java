package comp533.slave;

import java.io.Serializable;
import java.rmi.Remote;

public interface MapReduceSlave extends Runnable,Remote,Serializable {
	void waitSlave();
	void notifySlave();
	int getSlaveNumber();
}
