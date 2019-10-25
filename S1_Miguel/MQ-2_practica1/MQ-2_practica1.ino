#include <M5Stack.h>
#define pinADC 36

   float valorMedia = 0;
   float sumatorio = 0;
   int contador = 0;
   float valorMaximo = 0;
   float valorMinimo = 1000000;
   int v1 = 2.03;
   int v2 = 2330;
   float listademuestras[200];
   float aux;
   float k;
   float valorLeido;
   
void setup() {
 
 M5.begin();
 Serial.begin(9600);
 pinMode(OUTPUT, pinADC);
 M5.Lcd.println("MQ-2");
 
}

void loop() {

   k = v1/v2;                                                      
   valorLeido = analogRead(pinADC);   
   aux = k * valorLeido;
   listademuestras[contador]= aux;
   if(aux > valorMaximo){
      valorMaximo = aux;        
   }
   if(aux < valorMinimo){
      valorMinimo = aux;        
   }
   if(contador==100){
    for(int i = 0;i<=99; i++){
     M5.Lcd.println(listademuestras[i]);
    }
   }
   
   char valorPuerto = Serial.read();
   if(valorPuerto!=NULL){
   if(valorPuerto=='a'){
   for(int i = 0;i<=199; i++){
     Serial.println(listademuestras[i]);
   }
   }
   }

   sumatorio = sumatorio + aux;
   contador = contador +1;
   if(contador == 200){

      valorMedia = sumatorio/200;
    
   }
   Serial.println(valorMaximo);
   Serial.println(valorMedia);
   Serial.println(valorMinimo);
   delay(10);
   M5.update();
}
