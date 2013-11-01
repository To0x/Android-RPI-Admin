import RPi.GPIO as GPIO
import time

LED_PIN = 10
GPIO.setmode(GPIO.BOARD)
GPIO.setup(LED_PIN, GPIO.OUT)
DELAY = 3

while True:
    GPIO.output(LED_PIN, GPIO.HIGH)
    time.sleep(DELAY)
    GPIO.output(LED_PIN, GPIO.LOW)
    time.sleep(DELAY)
