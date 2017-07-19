Wilma Plugin Example - Random Error Generator
===============================

Purpose of this example
---------------------------------------
This plugin example was created to show how Wilma can be acted as a special kind of Chaos Monkey. See the original Chaos Monkey [tool here](https://github.com/Netflix/chaosmonkey), which tool is created by Netflix for AWS. 
So what kind of Chaos Monkey this example will show? This will simulate the following error situations:

 - E404 error will be received as response in a certain percentage of the requests, randomly
 - E500 error will be received as response for another percentage of the requests, randomly
 - response timeout will be cause for another percentage of the requests, randomly
 

Implementation
---------------------------------------
TBD.

Build
-----
```
#run extras: Random Error Generator example
./gradlew -b wilma-extras/random.error.generator/build.gradle clean build
```
After a successful run you will find the wilma plug-in in folder: wilma-extras/random.error.generator/build/libs as a jar file.

How to Use it?
-----------------------------------------
- Ensure that the compiled randomErrorGenerator jar is available for your Wilma instance
- Configure Wilma to use it (see example configuration above)

