import atexit
import os
import select
import socket
import sys

#import xmlrpc.server.SimpleXMLRPCServer
#import xmlrpc.server.SimpleXMLRPCRequestHandler


def prompt():
	sys.stdout.write('<You> ')
	sys.stdout.flush()

try:
	server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
except:
	print ("Failed to create Socket")
	sys.exit()

PORT = 9050
HOST = '192.168.0.113'
RECV_BUFFER = 4096

#server_socket.bind((HOST, PORT))
#server_socket.listen(10)

input = [server_socket, sys.stdin]

print ("Chat Program")
prompt()


try:

	while 1:
		
		inputready, outputready, exceptready = select.select(input,[],[])
		
		for sock in inputready:
			
			if sock == server_socket:
				client, address = server_socket.accept()
				input.append(client)
			
			elif sock == sys.stdin:
				data = sock.readline()
				for s in input:
					if s not in (server_socket, sys.stdin):
						s.send(data)
				prompt()
			else:
				data = sock.recv(RECV_BUFFER)
				if data:
					sys.stdout.write(data)
					prompt()
				else:
					msg = sys.stdin.readline()
					server_socket.send('\r<Server>: ' + msg)
					prompt()

except KeyboardInterrupt:
	server_socket.close()
	print("Socket closed")
