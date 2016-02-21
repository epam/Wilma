The easiest way of compilation:
run this comment for every src to be compiled, AFTER wilma built and released ( = gradlew build release):

javac -cp ../../../wilma-application/release/wilma-1.5.DEV.jar <javafile>

Then copy the class file into the message-sequence.jar file - located in the parent folder