import RPi.GPIO as GPIO
import time
import sys

LED_PIN = 23
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)

p = GPIO.PWM(LED_PIN, 100)
p.start(0)


try:
    while True:
        for dc in range(0, 101, 5):
            p.ChangeDutyCycle(dc)
            time.sleep(0.1)
        for dc in range(100, -1, -5):
            p.ChangeDutyCycle(dc)
            time.sleep(0.1)
except KeyboardInterrupt:
    p.stop()
    GPIO.cleanup()
    print('Programm beendet\n')