from Lenkung import Lenkung
from Motor import Motor
from Licht import Licht
from Bremse import Bremse

class Controler:
    
   
    def __init__(self):
        self.lenkung = Lenkung()
        self.motor = Motor()
        self.licht = Licht()
        self.bremse = Bremse()
        self.gearOld = False
        self.speedOld = 0
        pass
    
    
    def update(self, speed, angle, gear, licht):
        
        #print ("speed: ", speed)
        #print ("angle: ", angle)
        #print("gear: ", gear)
        
        """ TODO: Update an die Hardware des RaspCar weitergeben!! """
        if gear != self.gearOld:
            print ("gear ungleich gearOld")
            if gear:
                self.motor.move(2)
            else:
                self.motor.move(1)
            self.gearOld = gear
        
        if licht == 1:
            self.licht.lichtschalter(1)
        else:
            self.licht.lichtschalter(0)

        if speed != self.speedOld:
            self.motor.speed(speed)
            if self.speedOld > speed:
                self.bremse.bremslicht(1)
            else:
                self.bremse.bremslicht(0)
            self.speedOld = speed
        else: 
            self.bremse.bremslicht(0)
        self.lenkung.lenken(angle)
        pass
    
