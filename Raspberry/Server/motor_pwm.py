#!/usr/bin/python
import RPi.GPIO as GPIO
from time import sleep
import sys

MotorEnable1 = 23
Motor1_1A = 24
Motor1_2A = 18
MotorRechts = 1
Vorwaerts = 1
Rueckwaerts = 2
Stopp = 0

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(MotorEnable1, GPIO.OUT)
GPIO.setup(Motor1_1A, GPIO.OUT)
GPIO.setup(Motor1_2A, GPIO.OUT)
p = GPIO.PWM(MotorEnable1, 100)
p.start(0)



def motor(motor, move):
	if motor == 1:
		# Motor aus
		if move == 0:
			GPIO.output(Motor1_1A, GPIO.LOW)
			GPIO.output(Motor1_2A, GPIO.LOW)
		#Motor1 Vorwaerts
		if move == 1:
			GPIO.output(Motor1_1A, GPIO.HIGH)
			GPIO.output(Motor1_2A, GPIO.LOW)
		#Motor1 Rueckwaerts
		if move == 2:
			GPIO.output(Motor1_1A, GPIO.LOW)
			GPIO.output(Motor1_2A, GPIO.HIGH)

def move(cmd):
	"""Koordiniert Bewegen"""
	if cmd == 1: # vorwaerts
		motor(MotorRechts, Vorwaerts)
	if cmd == 2: # rueckwarts
		motor(MotorRechts, Rueckwaerts)
	if cmd == 0: # stop
		motor(MotorRechts, Stopp)


def main():
	GPIO.output(MotorEnable1, GPIO.HIGH)
	
	dc = 0
	pdc = 0
	try:
		while True:
			c = sys.stdin.read(1)
			if c == '0': move(0)
			if c == '1': move(1)
			if c == '2': move(2)
			if c == 'w': 
				pdc = pdc + 10
			if c == 's': 
				pdc = pdc - 10
			
			if pdc <= 0: 
				pdc = 0
			if pdc >= 100: 
				pdc = 100

			print ("pdc: " , pdc)
			p.ChangeDutyCycle(pdc)
			
	except KeyboardInterrupt:
		# Motor aus
		move(0)
        GPIO.cleanup()

if __name__ == '__main__':
	main()
