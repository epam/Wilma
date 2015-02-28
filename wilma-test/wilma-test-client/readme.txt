Running the client from command line:

1. Set the properties in wilma.testclient.properties
___________________________________________________________________________________________________________________________________________________________________________

2. If testserver.url is plain HTTP, the following command runs the client (and sends a request to the test-server):
	
	java -jar wilma-test-client-0.1.jar wilma.testclient.properties example2.xml > response_1
	
	- the first parameter is the properties file, the second is the xml file that will be sent in the request body
	- the test-client prints the received response to the console, it is recommended to write it to a file with '>' 
___________________________________________________________________________________________________________________________________________________________________________	

3. If testserver.url is HTTPS, and proxy usage is set to FALSE, the following command runs the client (and sends a request to the test-server):
	
	java -jar -Djavax.net.ssl.trustStore=certificate/wilmaKeystore.jks wilma-test-client-0.1.jar wilma.testclient.properties example2.xml > response_1
	
	- it's the same as in the first case, but an additional VM argument is needed, because the client needs to trust the unsigned certificate used by the TEST-SERVER.
	
___________________________________________________________________________________________________________________________________________________________________________	
	
4. If testserver.url is HTTPS, and proxy usage is set to TRUE, the following command runs the client (and sends a request to the test-server):
	
	java -jar -Djavax.net.ssl.trustStore=certificate/wilma.jks wilma-test-client-0.1.jar wilma.testclient.properties example2.xml > response_1
	
	- it's the same as in the second case, but the client now needs to trust the unsigned certificate used by WILMA.
	
___________________________________________________________________________________________________________________________________________________________________________