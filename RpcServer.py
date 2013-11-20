'''
Created on 19.11.2013

@author: Master
'''
from xmlrpc.server import SimpleXMLRPCServer

server = SimpleXMLRPCServer(('192.168.178.42', 9000), logRequests=True)
server.register_introspection_functions()


def adder_function(a,b):
    return a+b

server.register_function(adder_function, 'add')
print ("quad registered")
server.serve_forever()

