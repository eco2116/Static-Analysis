# Supress warning about no default IPv6 route
import logging
logging.getLogger("scapy.runtime").setLevel(logging.ERROR)

# Import scapy and libraries needed to generate random strings
from scapy.all import *
import random, string, sys

# Read in user-inputted ports
src_port = int(sys.argv[1])
dst_port = int(sys.argv[2])

# Hard-coded loopback address
loop_ip = "127.0.0.1"

# Setup the IP portion for loopback
ip = IP(src=loop_ip, dst=loop_ip)

# Part (1)
# Repeatedly send 20 TCP/IP packets with empty message bodies to ports incrementing from 3000 to 3020
# using the loopback address of 127.0.0.1 or localhost and source port provided by command line argument
for d_port in range(3000,3021):
    tcp = TCP(sport=src_port, dport=d_port)
    # Send TCP/IP packet with empty message body
    pkt = ip / tcp
    send(pkt)

# Part (2)
# Generate random printable string given a length
def random_str(length):
    return ''.join(random.choice(string.printable) for i in range(length))

# Send 5 TCP/IP packets to loopback address using user-inputted soruce and destination port
tcp = TCP(sport=src_port, dport=dst_port)
for i in range(0,5):
    rand_str = random_str(10)
    # Message body contains random string of length 10
    pkt = ip / tcp / rand_str
    send(pkt)

