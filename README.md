# DistributedMapReduce
-MapReduce implementation simulated in a distributed setting using Java's concurrency APIs and JavaRMI. Also includes a variant of MapReduce (Facebook's mutual friend algorithm).<br/>
-Aside from bounded buffers (producer/consumer problem), master/slave relationship, barrier synchronization, fork/join synchronization, and a partitioning abstraction seen in Hadoop,
this implementation uses Remote Procedure Calls (Java RMI), enabling distribution through registering a remote server proxy to keep track of remote independent clients.
