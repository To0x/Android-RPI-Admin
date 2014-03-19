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

class Motor:
    def __init__(self):
        GPIO.output(MotorEnable1, GPIO.HIGH)
        self.motor(Vorwaerts)
        pass    
    
    def motor(self, move):
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

    def move(self, cmd):
        """Koordiniert Bewegen"""
        if cmd == 1: # vorwaerts
            self.motor(Vorwaerts)
        if cmd == 2: # rueckwarts
            self.motor(Rueckwaerts)
        if cmd == 0: # stop
            self.motor(Stopp)

    def speed(self, speed):
        if speed < 0:
            speed = 0
        if speed > 100:
            speed = 100
        p.ChangeDutyCycle(speed)
    
    def quit(self):
        pass
