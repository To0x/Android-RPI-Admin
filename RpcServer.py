'''
Created on 19.11.2013

@author: Master
'''
from SimpleXMLRPCServer import SimpleXMLRPCServer
#from xmlrpc.server import SimpleXMLRPCServer
from Cam import Cam

server = SimpleXMLRPCServer(('141.45.203.191', 9000))
server.register_introspection_functions()


def adder_function(a,b):
    return a+b

def startCam(ipAdress, port):
    c = Cam(ipAdress,port)
    c.startCam()
    pass

server.register_function(adder_function, 'add')
server.register_function(startCam, 'startCam')
server.serve_forever()

