import json
import socket
from Controler import Controler
import threading

class Receiver(threading.Thread):
    
    lock = threading.Lock()
    
    def __init__(self,controler,  udpIpAdress, udpPort):
        threading.Thread.__init__(self)
        self.UDP_IP = udpIpAdress
        self.UDP_PORT = udpPort
        self.controler = controler
        self.__stop = False
        
        
        self.sock = socket.socket(socket.AF_INET, # Internet
                     socket.SOCK_DGRAM) # UDP
        
        self.sock.bind((self.UDP_IP, self.UDP_PORT))
        self.sock.settimeout(1)
        
        pass
    
    def run(self):
        while True:            
            """ funtkioniert noch nicht - warum?! """
            if (self.__stop is True):
                self.sock.close()
                print("connection closed!")
                break  
            
            print ("waiting - stop with 'q'")            
            
            try:
                data, addr = self.sock.recvfrom(1024) # buffer size is 1024 bytes
            except:
				self.controler.update(0,0,False,0)
                continue
            
            Receiver.lock.acquire()
            
            strdata = str(data)#[2:-1] # cut of b' at the beginning and ' at the end
            #print("Data " + strdata + " und " + str(data))
            decoded = json.loads(strdata)
            speed = decoded["speed"]
            angle = decoded["angle"]
            gear = decoded["gear"]
            light = decoded["light"]
            self.controler.update(speed,angle,gear,light)
            
            Receiver.lock.release()
            
    def stop(self):
        self.__stop = True

controler = Controler()

thread = Receiver(controler, "192.168.55.1", 5001)
thread.start()

while True:
    eingabe = input()
    
    if (eingabe.lower() in ['q']):
        thread.stop()
        break
