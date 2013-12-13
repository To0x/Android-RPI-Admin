'''
Created on 19.11.2013

@author: Master
'''
from SimpleXMLRPCServer import SimpleXMLRPCServer
from threading import Thread
from Cam import Cam
from test.test_typechecks import Integer

server = SimpleXMLRPCServer(('141.45.203.191', 9000))
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
	
server.register_function(adder_function, 'add')
server.register_function(startCam, 'startCam')
server.register_function(startAudio, 'audio')
server.serve_forever()

