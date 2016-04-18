import sys
from scapy.all import *

f = open('inpartb.txt', 'r')

src_ip = f.readline().rstrip()
src_port = int(f.readline().rstrip())

dst_ip = f.readline().rstrip()
dst_port = int(f.readline().rstrip())

get_body = f.read()

ip = IP(src=src_ip, dst=dst_ip)
tcp = TCP(sport=src_port, dport=dst_port)

pkt = ip / tcp / get_body

if pkt:
    show(pkt)
    print str(pkt)

send(pkt)

f.close()
