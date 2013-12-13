#include <wiringPi.h>
#include <softPwm.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#define LED_Pin 4
#define DELAY 10

int main(void){
    int i = 0;
    printf("Raspberry Pi PWM-Programm mit wiringPi\n");
    if(wiringPiSetup() == -1)
		exit (EXIT_FAILURE);
    pinMode(LED_Pin, PWM_OUTPUT);
    softPwmCreate(LED_Pin, 0, 100);

   for(;;){
	for(i = 0; i<=100; i++){
	   softPwmWrite(LED_Pin, i);
	   delay(DELAY);
	}

	for(i = 100; i>=0; i--){
	   softPwmWrite(LED_Pin, i);
	   delay(DELAY);
	}

   }
   return EXIT_SUCCESS;

}
