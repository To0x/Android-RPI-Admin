#!/usr/bin/python
import RPi.GPIO as GPIO
import sys

licht1 = 4
licht2 = 17

GPIO.setmode(GPIO.BCM)
GPIO.setup(licht1, GPIO.OUT)
GPIO.setup(licht2, GPIO.OUT)

class Licht:
    def __init__(self):
        GPIO.output(licht1, GPIO.LOW)
        GPIO.output(licht2, GPIO.LOW)
        pass

    def lichtschalter(self, lichtio):
        if lichtio == 1:
            GPIO.output(licht1, GPIO.HIGH)
            GPIO.output(licht2, GPIO.HIGH)
        if lichtio == 0:
            GPIO.output(licht1, GPIO.LOW)
            GPIO.output(licht2, GPIO.LOW)