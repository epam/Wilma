Wilma Example - Short Circuit
=============================

This readme file is under construction.
---------------------------------------

How to list the available services:
GET/POST http://localhost:1234/config/public/services/

Get the list of the captured and cached messages
-----
If the same request arrives, 
the relevant cached message will be sent back by the stub.
```
GET http://localhost:1234/config/public/services/ShortCircuitInterceptor/circuits 
```

The list is presented as an array of key-value pairs, 
where the key is an unique id, meanwhile the value is a hash-key to identify the request.
In the Short Circuit logic, this hash-key is used to identify the cached response.

Save the actual cache into a folder
-----------------------------------
You may save the actual internal Short Circuit cache onto the disk. 
Just specify a folder name in the following request:

```
POST http://localhost:1234/config/public/services/ShortCircuitInterceptor/circuits?folder={toFolder}
```

The response is the following
```
{
  "resultsSuccess": "Map saved as: messages/{toFolder}/sc0_*.json files"
}
```
As you see, the cache is saved to "messages/{toFolder}" folder in files, by using the "sc0_*.json" pattern.

Load a cache from a folder
--------------------------
**NOT YET IMPLEMENTED**
To load a Short Circuit cache, just specify a folder that contains Short Circuit files in the following form:
```
GET http://localhost:1234/config/public/services/ShortCircuitInterceptor/circuits?folder={toFolder}
```

Clean-up the cache
------------------
You may clean-up (delete) the cache by calling the Short Circuit URL with DELETE method

So just call this:
```
DELETE http://localhost:1234/config/public/services/ShortCircuitInterceptor/circuits 
```
and the cache will be empty - that means all the preserved and cached response will be forgotten.

The call will response with the actual (empty) map.

Delete one specific entry from te cache
--------------------------
**NOT YET IMPLEMENTED**
The call will be:
```
DELETE http://localhost:1234/config/public/services/ShortCircuitInterceptor/circuits?id={idInShortCircuitMap} 
```
