# Supress warning about no default IPv6 route
import logging
logging.getLogger("scapy.runtime").setLevel(logging.ERROR)

import sys
from scapy.all import *

# Read in from input file
f = open('inpartb.txt', 'r')

# Store source IP and port, which are the fist two lines of the file
src_ip = f.readline().rstrip()
src_port = int(f.readline().rstrip())

# Store destination IP and port, which are the second two lines of the file
dst_ip = f.readline().rstrip()
dst_port = int(f.readline().rstrip())

# Read the rest of the file, which is the HTTP get message body.
get_body = f.read()

# We are done reading from the input file
f.close()

# Create IP and TCP portions of packet
ip = IP(src=src_ip, dst=dst_ip)
tcp = TCP(sport=src_port, dport=dst_port)

# Send IP/TCP packet with HTTP get message body
pkt = ip / tcp / get_body

# Display the packet using "show" and print the string version of the packet
if pkt:
    pkt.show()
    print str(pkt)

# Send the packet
send(pkt)
