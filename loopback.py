# Supress warning about no default IPv6 route
import logging
logging.getLogger("scapy.runtime").setLevel(logging.ERROR)

from scapy.all import *
import random, string, sys

src_port = int(sys.argv[1])
dst_port = int(sys.argv[2])

loop_ip = "127.0.0.1"

ip = IP(src=loop_ip, dst=loop_ip)

# Part (a)
for d_port in range(3000,3020):
    tcp = TCP(sport=src_port, dport=d_port)
    pkt = ip / tcp
    send(pkt)

# Part (b)
def random_str(length):
    return ''.join(random.choice(string.printable) for i in range(length))

tcp = TCP(sport=src_port, dport=dst_port)
for i in range(0,5):
    rand_str = random_str(10)
    print("Random string: " + rand_str)
    pkt = ip / tcp / rand_str
    send(pkt)

