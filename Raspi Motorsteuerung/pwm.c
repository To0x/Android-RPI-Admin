#include <wiringPi.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#define LED_Pin 1
#define DELAY 1

int main(void){
    int i = 0;
    printf("Raspberry Pi PWM-Programm mit wiringPi\n");
    if(wiringPiSetup() == -1)
	exit (EXIT_FAILURE);
    pinMode(LED_Pin, PWM_OUTPUT);

   for(;;){
	for(i = 0; i<=1023; i++){
	   pwmWrite(LED_Pin, i);
	   delay(DELAY);
	}

	for(i = 1023; i>=0; i--){
	   pwmWrite(LED_Pin, i);
	   delay(DELAY);
	}

   }
   return EXIT_SUCCESS;

}