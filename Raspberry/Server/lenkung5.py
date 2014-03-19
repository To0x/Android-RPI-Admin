from RPIO import PWM
import time
Pin = 25
servo = PWM.Servo()

i = 0
while (i < 2):
	servo.set_servo(Pin, 1250)
	time.sleep(1)
	servo.set_servo(Pin, 1500)
	time.sleep(1)
	servo.set_servo(Pin, 1700)
	time.sleep(1)
	servo.set_servo(Pin, 1950)
	time.sleep(1)
	servo.set_servo(Pin, 1600)
	time.sleep(1)
	i = i + 1
servo.stop(Pin)

