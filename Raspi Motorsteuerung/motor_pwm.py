#!/usr/bin/python
import RPi.GPIO as GPIO
from time import sleep
import sys

MotorEnable1 = 14
Motor1_1A = 15
Motor1_2A = 18
MotorEnable2 = 25
Motor2_1A = 24
Motor2_2A = 23
MotorRechts = 1
MotorLinks = 2
Vorwaerts = 1
Rueckwaerts = 2
Stopp = 0

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(MotorEnable1, GPIO.OUT)
GPIO.setup(Motor1_1A, GPIO.OUT)
GPIO.setup(Motor1_2A, GPIO.OUT)
GPIO.setup(MotorEnable2, GPIO.OUT)
GPIO.setup(Motor2_1A, GPIO.OUT)
GPIO.setup(Motor2_2A, GPIO.OUT)
p = GPIO.PWM(MotorEnable1, 100)
p.start(0)
q = GPIO.PWM(MotorEnable2, 100)
q.start(0)


def motor(motor, move):
	if motor == 1:
		# Motor aus
		if move == 0:
			GPIO.output(Motor1_1A, GPIO.LOW)
			GPIO.output(Motor1_2A, GPIO.LOW)
		#Motor1 rechts drehen
		if move == 1:
			GPIO.output(Motor1_1A, GPIO.HIGH)
			GPIO.output(Motor1_2A, GPIO.LOW)
		#Motor1 links drehen
		if move == 2:
			GPIO.output(Motor1_1A, GPIO.LOW)
			GPIO.output(Motor1_2A, GPIO.HIGH)
	if motor == 2:
		# Motor aus
		if move == 0:
			GPIO.output(Motor2_1A, GPIO.LOW)
			GPIO.output(Motor2_2A, GPIO.LOW)
		#Motor1 rechts drehen
		if move == 1:
			GPIO.output(Motor2_1A, GPIO.HIGH)
			GPIO.output(Motor2_2A, GPIO.LOW)
		#Motor1 links drehen
		if move == 2:
			GPIO.output(Motor2_1A, GPIO.LOW)
			GPIO.output(Motor2_2A, GPIO.HIGH)

def move(cmd):
	"""Koordiniert Bewegen"""
	if cmd == 1: # links
		motor(MotorRechts, Vorwaerts)
		motor(MotorLinks, Rueckwaerts)
	if cmd == 2: # rechts
		motor(MotorRechts, Rueckwaerts)
		motor(MotorLinks, Vorwaerts)
	if cmd == 3: # vorwaerts
		motor(MotorRechts, Vorwaerts)
		motor(MotorLinks, Vorwaerts)
	if cmd == 4: # rueckwarts
		motor(MotorRechts, Rueckwaerts)
		motor(MotorLinks, Rueckwaerts)
	if cmd == 0: # stop
		motor(MotorRechts, Stopp)
		motor(MotorLinks, Stopp)


def main():
	GPIO.output(MotorEnable1, GPIO.HIGH)
	GPIO.output(MotorEnable2, GPIO.HIGH)
	
	dc = 100
	try:
		while True:
			c = sys.stdin.read(1)
			if c == '0': move(0)
			if c == '1': move(1)
			if c == '2': move(2)
			if c == '3': move(3)
			if c == '4': move(4)
			if c == '+': 
				dc = dc + 10
			if c == '-': 
				dc = dc - 10
			if dc <= 0: 
				dc = 0
			if dc >= 100: 
				dc = 100
			p.ChangeDutyCycle(dc)
			q.ChangeDutyCycle(dc)
			
	except KeyboardInterrupt:
		# Motor aus
		move(0)
        GPIO.cleanup()

if __name__ == '__main__':
	main()
