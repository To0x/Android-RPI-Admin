'''
Created on 20.11.2013

@author: Master
'''


import cv2
import socket

class Cam:
    def __init__(self, udpAdress, udpPort):
        self.fps = 10
        self.waitTime = 60 / self.fps
        self.adress = udpAdress
        self.port = udpPort
        self.c = cv2.VideoCapture(0)
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
    def startCam(self):
        while(True):
            _,f = self.c.read()
            self.sock.sendto("hallo Welt", (self.adress, self.port))
            cv2.imshow('bla blup',f)
            if cv2.waitKey(self.waitTime)==27:
                break
                
        cv2.destroyAllWindows()
            

#c = Cam('127.0.0.1',5006)
#c.startCam()