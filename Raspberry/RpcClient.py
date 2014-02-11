'''
Created on 19.11.2013

@author: Master
'''

import xmlrpc
import socket
#import cv2

s = xmlrpclib.ServerProxy('http://141.45.203.191:9000')
print (s.system.listMethods())
print (s.add(1,2))

udp_ip = '127.0.0.1'
udp_port = 50013

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((udp_ip, udp_port))

#s.startCam(udp_ip, udp_port)

while (True):
    data, addr = sock.recvfrom(1024)
    print (data)
    