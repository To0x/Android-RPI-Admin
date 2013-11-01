#!/usr/bin/python
import RPi.GPIO as GPIO
from time import sleep
import sys

MotorEnable = 14
Motor1_1A = 15
Motor1_2A = 18
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(MotorEnable, GPIO.OUT)
GPIO.setup(Motor1_1A, GPIO.OUT)
GPIO.setup(Motor1_2A, GPIO.OUT)

def motor(move):
	# Motor aus
	if move == 0:
		GPIO.output(Motor1_1A, GPIO.LOW)
		GPIO.output(Motor1_2A, GPIO.LOW)
	#Motor rechts drehen
	if move == 1:
		GPIO.output(Motor1_1A, GPIO.HIGH)
		GPIO.output(Motor1_2A, GPIO.LOW)
	#Motor links drehen
	if move == 2:
		GPIO.output(Motor1_1A, GPIO.LOW)
		GPIO.output(Motor1_2A, GPIO.HIGH)

def main():
	GPIO.output(MotorEnable, GPIO.HIGH)
	try:
		while True:
			c = sys.stdin.read(1)
			if c == '0': motor(0)
			if c == '1': motor(1)
			if c == '2': motor(2)
	except KeyboardInterrupt:
		# Motor aus
		motor(0)
        GPIO.cleanup()

if __name__ == '__main__':
	main()
