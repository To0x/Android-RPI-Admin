from RPIO import PWM
import time
Pin = 25
servo = PWM.Servo()

class Lenkung:
    def __init__(self):
        self.multiplikator = 10
        self.mostLeft = 1250
        self.mostRight = 1950
        self.middle = 1600
        pass
    
    def lenken(self, angle):
        lenkWinkel = self.middle - angle * self.multiplikator
        #print (lenkWinkel)
        if lenkWinkel < self.mostLeft:
            lenkWinkel = self.mostLeft
            
        if lenkWinkel > self.mostRight:
            lenkwinkel = self.mostRight
        servo.set_servo(Pin, lenkWinkel)
        #time.sleep(1)
        pass
    
    def quit():
        
        pass

