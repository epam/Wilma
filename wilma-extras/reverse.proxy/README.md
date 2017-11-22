Wilma Example - Reverse Proxy
=============================

Purpose of this example
---------------------------------------
To show the capability of Wilma by building up a reverse-proxy solution as a plugin for Wilma.

Implementation
---------------------------------------
The `FowardProxyInformation` class contains information about a specific reverse proxy rule. This rule contains:
- an id
- the original target
- the real target
When Wilma detects (see `ReverseProxyInterceptor` class) that in the original URL the "original target" can be found, 
then replaces it with the "real target". It works even if the source protocol is https meanwhile the target is http - or vica-versa.
The `ReverseProxyService` class acts as External Service, and gives the opportunity to add a new and remove an existing 
reverse-proxy entry, and to save/load the actual reverse-proxy rule list into/from a folder.

**NOTE: when the "real target" is specified, use real IP address !** In the actual implementation there is no name resolution during the reverse-proxy event.

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the ReverseProxy won't work properly (no message destination change will be done at all).

REST Interface of Reverse Proxy plugin
---------------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
Also, in the examples below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

Get the full list of the actual reverse-proxy rule map
---
```
GET http://localhost:1234/config/public/services/ReverseProxyInterceptor/reverse-proxy
```

Clean-up the reverse-proxy rule map
------------------
You may clean-up (delete) the reverse-proxy map by calling the ReverseProxy URL with DELETE method

So just call this:
```
DELETE http://localhost:1234/config/public/services/ReverseProxyInterceptor/reverse-proxy
```
and the reverse-proxy map will be empty - that means no rule will be applied by the reverse-proxy.

The call will response with the actual (empty) map.

Delete one specific entry from the cache
--------------------------
To delete a specific cache entry, call this:
```
DELETE http://localhost:1234/config/public/services/ReverseProxyInterceptor/reverse-proxy/{idOfReverseProxyMapEntry}
```

Save the actual reverse-proxy map into a folder
-----------------------------------
You may save the actual internal reverse-proxy map onto the disk.
Just specify a folder name in the following request:

```
POST http://localhost:1234/config/public/services/ReverseProxyInterceptor/reverse-proxy?folder={toFolder}
```
And the ReverseProxy will create the specified folder in the folder used to store the message logs, and will save the reverse-proxy map into it,
one file for every map entry.

The response to save request is the following (example)
```
{
  "resultsSuccess": "ReverseProxy rule map saved as: messages/{toFolder}/sc0_*.json files"
}
```
As you see, the map is saved to "messages/{toFolder}" folder in files, by using the "sc0_*.json" pattern.

If you repeat the save request, the plugin will save the map again, by using different names.

Load a reverse-proxy map  from a folder
--------------------------
To load a previously saved reverse-proxy map, just specify a folder that contains the saved files in the following form:
```
GET http://localhost:1234/config/public/services/ReverseProxyInterceptor/reverse-proxy?folder={fromFolder}
```
NOTE: This load does not clean up the exiting cache, rather adds new entries to the cache.
In case a cache entry already exists, it will be overwritten. If you need nothing else just the loaded files in the cache, clean-up the cache first.
