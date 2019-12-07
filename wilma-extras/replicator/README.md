Wilma Example - Replicator
==========================

Purpose of this example
-----------------------

This example shows how to send a request not just to the original target (by using the proxy part), but in the meantime send the same request to an alternative/secondary target.
When response arrives from both the original target and from the alternative/secondary target too, the requests and the responses may be compared.

With this approach you may:

* compare the behavior of two different version of the same target components (webservice for example)
* compare the behavior of an existing (maybe legacy) service with a new service, that is planned to be replacing the existing service

Implementation
--------------

Now let's see how it works.

An interceptor (ReplicatorInterceptor) intercepts the message, with the help of the interceptor configuration in Wilma, 
decides if the request should be replicated and sent to a secondary server or not.
If yes, copies the original message, sets its new target url, and puts it into a Replicator Queue. By using such queue we will not block the flow of the original request.

The queue consumer that gets notification automatically that there is a message to be sent to the secondary server, prepares the request, 
sends it to the secondary server, and finally receives the response.

Finally the sent and received messages are saved onto the disk by using Wilma's built in message logging feature (via another queues). 
These secondary responses will get the same names as the original request/response pair but with prefix "REPLICA_".

Using these files, you may evaluate the request/response pairs and act as necessary.

Build
-----
```
#run extras: Replicator example
./gradlew -b wilma-extras/replicator/build.gradle clean build
```
After a successful run you will find the wilma plug-in in folder: wilma-extras/replicator/build/libs as a jar file.

Test Setup
----------
Prepare the following test environment:

- We need a client: use a web browser for that and set its proxy to use wilma (default wilma proxy port is: 9092, and the host is where wilma is running)
- We need a web server that will act as primary target - let's use any public web server
- We need another web server that will act as secondary target - this will receive the replicated requests. In our example, use Wilma's built-in wilma-test-server. 
You may find information on how to build wilma-test-server on web page: [Optional Build Step: Building Wilma Test Client and Test Server for Wilma](https://github.com/epam/Wilma/wiki/DEV,-Build-from-Scratch#optional-build-step-building-wilma-test-client-and-test-server-for-wilma) 
and an example on how to start it on web page: [Start Wilma Environment](https://github.com/epam/Wilma/blob/master/config/environment/start-wilma-environment.sh) 

Then start the environment:

- First have the replicator plugin compiled (see how to build above) and a prepared Wilma config file at your hand (you may find a proper config file in folder: wilma-extras/replicator/src/main/resources) 
- Start Wilma first, then the wilma-test-server
- Check with the browser that you can reach web sites, especially this site: https://github.com/epam/Wilma/tree/replicator/wilma-extras/replicator (continue only if it works properly)
- Upload the replicator-plug-in
- Upload the config file (continue only if both of them loaded properly)
- In your browser, try to load https://github.com/epam/Wilma/tree/replicator/wilma-extras/replicator - the page should be loaded normally
- If you check the content of the config file, you will recognize the interceptor part:
```
    "interceptors": [
      {
        "name": "Replicator",
        "class": "com.epam.wilma.extras.replicator.interceptor.ReplicatorInterceptor",
        "parameters": [
          {
            "name": "https://github.com/epam/Wilma/tree/master/wilma-extras/replicator",
            "value": "http://localhost:9090/replicator"
          }
        ]
      }
    ]
```
- This means that the ReplicatorInterceptor will be called when a message arrives to the proxy. The interceptor has a parameter, its name specifies the URL of the target. 
If a request arrives that matches to the URL, the ReplicatorInterceptor will replicate the request and send the request to the secondary server that is given as value of the parameter, 
and right now it is: http://localhost:9090/replicator - in normal case the wilma-test-server will receive the replicated request and will send back a fix message.
- Search for the replicated request/response message pairs among the saved message files in messages folder of Wilma, and you will find them. The filenames will start with "REPLICA_" prefix.

Have fun!
