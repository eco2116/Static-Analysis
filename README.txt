Evan O'Connor (eco2116)
Network Security
Programming Assignment 3

README.txt

#########################################################
# Part 1
#########################################################

-----Analysis using ssdeep-----

I ran ssdeep in a directory with only the files prog1-prog6 and the unpacked version of prog4. The -p flag
indicates pretty matching mode (includes all matches and compares all files in a directory). The -a flag
displays all matches, regardless of score.

Example command:
ssdeep -pa *

-----Analysis using strings-----

Example command:
strings prog1

-----Analysis using n-gram distribution-----

In my discussion PDF document, I have compiled the top 20 n-grams for various n and slide values, as indicated
in the assignment. I also compiled the top 3 n-grams for these n and slide values into one table (Table 11), which
you should look at to back up my explanation of the similarities of programs using n-gram analysis. Below, I will
explain my implementation of the project and the classes I used.

* Analyzer.java
This class contains the main method that runs my program with the following parameters:

java Analyzer <n> <s> <inputFile> <outputFile>

where n is the size of the n-gram to track, and s is the slide value. The input file is the file that will be analyzed
and the output file will contain the top 20 n-grams after the program finishes running. I first validate the input
parameters to ensure the n is either 1, 2 or 3 and that the slide is less than or equal to n and greater than 0.
Then, I start a timer. My program uses a rolling buffer input stream class which periodically fills a buffer from a
stream so that we can continuously read n-grams, even if the file is very large. This helper class keeps track of
a starting and ending mark for where the sliding window is at the current time. This allows me to easily advance
my sliding window and continuously update a hash map that keeps track of the counts of all n-grams seem so far
in the file in their respective counts. I use a utility class with a sort function to sort a HashMap by its values.
I use the NGram helper class to ensure ties in counts result in the sorting indicated in the assignment (e.g.: for n=3,
<0x00, 0x00, 0x00> would come before <0x00, 0x00, 0x01>). I then print to the indicated output file the top 20 n-grams
sorted first by increasing counts, and with ties, sorted by hex value as indicated in the assignment. I then
print out the total elapsed time taken to analyze the n-grams. Even with the largest file (prog1), the longest
time taken for a trial (where n=3 and s=1) was under .13 seconds.

* NGram.java
This helper class is a wrapper around an array of bytes that holds an n-gram. I needed to implement custom functions
to override the default equals, comparator, hash code, and to string functions. I used the Arrays class to ensure
that this wrapper around an n-gram would be correctly sorted as indicated in the assignment. (e.g.: for n=3,
<0x00, 0x00, 0x00> would come before <0x00, 0x00, 0x01>).

* RollingBufferInputStream.java
This helper class keeps at least N bytes available in a buffer and reads bytes from an input stream. The resource
I used to develop this class is listed in my resources section. For a more detailed description of its functionality,
see the attached link below.

#########################################################
# Part 2
#########################################################

-----ping.py (2a)-----

In this python script, I use scapy to send an ICMP (ping) message given an IP address. You must have root access
to run this script. The output from tcpdump for the packet sent and the response received is in ping_dump.txt.

Example command:
python ping.py 160.39.110.199

-----get.py (2b)-----

In this python script, I read source IP and port and destination IP and port from a file named inpartb.txt. This
input file also contains a sample get message body, which is read in and used as the body of a new get message
which is sent to the chosen destination. The output from tcpdump is stored in the file in.txt.

Example command:
python get.py

-----loopback.py (2c)-----

This python script first sends TCP/IP packets with empty message bodies to destination ports ranging from 3000 to 3020,
one message for each port. The loopback IP is hardcoded as 127.0.0.1. The user will input the source port to be used
in part 1. For part 2, random strings of length 10 are generated and sent as the message body of TCP/IP packets
on the loopback IP address, using the user-inputted source and destination ports.

Example command:
python loopback.py 8001 8002

#########################################################
# Resources used
#########################################################

For scapy:
http://www.secdev.org/projects/scapy/build_your_own_tools.html

For generating random strings of given lenght in python:
http://stackoverflow.com/questions/2030053/random-strings-in-python

For implementing a rolling buffer input stream:
http://tutorials.jenkov.com/java-howto/iterating-streams-using-buffers.html

For sorting hashmap by values:
http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java

For sorting the bytes by hex value:
http://stackoverflow.com/questions/5108091/java-comparator-for-byte-array-lexicographic