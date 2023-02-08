Wilma Example - ResponseAutoFixer - Interface
=============================

Purpose of this example
---------------------------------------
The purpose of this example to implement a very simple response interceptor, that generates a response in case the original response seems problematic. 

Implementation
---------------------------------------

| Property name | Meaning                                                                                                                                                                         | Example value             |
|---------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| path          | This is used as URL to identify the communication channel.                                                                                                                      | "http://service.to.call/" |
| successCodes  | The listed response codes are considered as normal response codes. When Circuit Breaker detects a result code that is not in this list, that is considered as a communication error. | "200,201,303"             |
| responseCode  | To be used in the answer as status code, when it is replaced.                                                                                                                   | "404"                     |
| reasonPhrase  | To be used in the answer as reason phrase, when it is replaced.                                                                                                                 | "Not found"               |
| responseType  | To be used in the answer as mime type of the response, when it is replaced.                                                                                                     | text/plain                |
| response      | To be used in the answer as response content, when it is replaced.                                                                                                              | "Server is down."         |

The response interceptor
 * In case the request of the response fits to the specific path
   * checks if the response is acceptable or not (see successCodes property). If not, 
   * replaces the answer with the given response Code, type and content

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the Circuit Breaker won't work properly (the circuit will never be broken).
