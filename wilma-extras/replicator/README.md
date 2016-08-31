Wilma Example - Replicator
==========================

Purpose of this example
-----------------------

This example show how to send a request not just to the original target (by using the proxy part), but in the meantime send the same request to an alternative/secondary target.
When response arrives from both the original target and from the alternative/secondary target too, the requests and the responses may be compared.

With this approach you may:

* compare the behavior of two different version of the same component (webservice for example)
* compare the behavior of an existing (maybe legacy) service with a new service 

Implementation
--------------

Now let's see how it works.

An interceptor (ReplicatorInterceptor) intercepts the message, with the help of the interceptor configuration in Wilma, decides if the request should be replicated and sent to a secondary server or not.
If yes, copy the original message, set its new target url, and put it into a Replicator Queue. By using such queue we will not block the original request flow.

The queue consumer that gets notification automatically that there is a message to be sent to the secondary server, prepares the request, sends itt o the secondary server, receives the response.

Finally the send and received messages are saved into disk by using Wilma's built in message logging feature (via another queues). These secondary responses will get the same names as the original request/response pair but with profix "REPLICA_".

Build
-----

TBC... 

Test Setup
----------

To test it, you need wilma-test-server running.
TBC...

Have fun!
