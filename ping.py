# Supress warning about no default IPv6 route
import logging
logging.getLogger("scapy.runtime").setLevel(logging.ERROR)

# Import scapy
import sys
from scapy.all import sr1,IP,ICMP

# Send ping given IP address as first cmd line arg
p = sr1(IP(dst=sys.argv[1])/ICMP())
if p:
	p.show()