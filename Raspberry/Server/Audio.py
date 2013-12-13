'''
Created on 09.12.2013

@author: Master
'''

from threading import Thread
import time

class AudioTypes():
    unidirectional = 0
    bidirectional = 1
    
class Audio(Thread):
    def __init__(self, AudioTypes=AudioTypes.unidirectional):
        Thread.__init__(self) 
        pass
    
    def run(self):
        for x in range (0,10):
            print (x)
            time.sleep(0.1)
        
        
threads = []
        
thread = Audio()
threads += [thread]

thread = Audio()
threads += [thread]

thread = Audio()
threads += [thread]

thread = Audio()
threads += [thread]

thread = Audio()
threads += [thread]

for t in threads:
    t.start()

