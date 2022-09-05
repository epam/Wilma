The easiest way of compilation:
run this command for every src to be compiled, AFTER wilma built and released ( = ./gradlew build release):

javac -cp ./../../../../wilma-application/release/wilma-2.0.DEV.jar <javafile>

(path to the wilma release and its version may vary)

when compiling jars, use this kind of command to compile a class + be under 'src' folder
javac -cp "./../../../wilma-application/release/wilma-2.0.DEV.jar:." ./com/epam/sandbox/responseFormatter/TestResponseFormatterJared.java
then copy the class files into the jar file

For the InvalidClass - ensure a class content that can be compiled with higher java version that is used for running Wilma itself
E.g. if Wilma uses JDK 15, compile the class with JDK 18 (javac file).