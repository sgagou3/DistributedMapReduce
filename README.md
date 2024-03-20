# DistributedMapReduce
An implementation of Map Reduce in a distributed setting. Also includes a rendition on Facebook's mutual friend algorithm, which is a variant of Map Reduce.<br />
Aside from bounded buffers (producer/consumer problem), master/slave relationship, barrier synchronization, fork/join synchronization, and a partitioning abstraction seen in Hadoop,<br/>
this implementation uses Remote Procedure Calls (Java RMI), enabling distribution through registering a remote server proxy to keep track of remote independent clients.
