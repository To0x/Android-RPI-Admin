'''
Created on 19.11.2013

@author: Master
'''

from xmlrpc.server import SimpleXMLRPCServer, SimpleXMLRPCRequestHandler
from threading import Thread
#from Cam import Cam
from test.test_typechecks import Integer
import socket

#server = SimpleXMLRPCServer(('141.45.203.191', 9000),requestHandler=RequestHandler)

''' The Ip Adress of the WLAN-Interface '''
server = SimpleXMLRPCServer(("localhost", 9000),
                            requestHandler=SimpleXMLRPCRequestHandler,allow_none=False)

server.register_introspection_functions()

def adder_function(a,b):
    return a+b

def startCam(ipAdress, port):
    c = Cam(ipAdress,port)
    c.startCam()
    pass
	
def startAudio(audioType, ipAdress, port):
	audio = Audio(audioType,ipAdress,port)
	audio.start()

def startControl(ipAdress):
    pass

	
server.register_function(adder_function, 'add')
print ("add registered")
server.register_function(startCam, 'startCam')
print ("start cam registered")
server.register_function(startAudio, 'audio')
print ("audio registered")
server.register_function(startControl, 'control')
print ("control registered")
server.serve_forever()
