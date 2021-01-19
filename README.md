Date: Feb 2020 
## Project

Java to C Remote procedure call (RPC)

## Languages

1- Server code is in C.

2- Client code is in Java (c_char.java, c_int.java, GetLocalOS.java, GetLocalTime.java, Test.java)

## To run (please run in pyrite or Mac):

1- Run server.c first by opening a terminal and simply typing "make" then "./server"

2- Then you could run Test.java by entreating the src folder and typing "javac Test.java" then "java Test"

3- You will be able to see the result of my test that I wrote as follows: 


* A message will appear indicating that A TCP connection has been made since my GetLocalTime object called "execute"

* The current time will appear as a one int in the following format:HHMMSS.Please note that it will be HMMSS if testing between 1 am -9:59 am since integers can't have leading zeros because they're meaningless. Please also note the if time is not valid then it will be just 0. A way to test this is to go to my GetLocalTime class and change the command to an invalid command in line 54 (any commands other than strictly "GetLocalTime" or "GetLocalOS" are considered invalid).

* The validity of my valid instance variable of GetLocalTime class. It will be 'y' if it's valid, otherwise 'n'.

* A message will appear indicating that A TCP connection has been made since my GetLocalOS object called "execute"

* The current OS will be displayed by looping through my c_char array and printing character by character (strings in C are character arrays after all). If OS is invalid, then the OS will just be 'nnnnnnnnnnnnn' to indicate that. A way to test this is to go to my GetLocalOS class and change the command to an invalid command in line 54 (any commands other than strictly "GetLocalTime" or "GetLocalOS" are considered invalid).

* The validity of my valid instance variable of GetLocalOS class. It will be 'y' if it's valid, otherwise 'n'.


Thank you!!

Fadel
