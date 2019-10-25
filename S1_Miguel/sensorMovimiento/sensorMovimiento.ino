#include <M5Stack.h>

const int PIRPin = 5;         // pin de entrada (for PIR sensor)

int pirState = LOW;           // de inicio no hay movimiento
int val = 0;                  // estado del pin

void setup()
{
  M5.begin();
  M5.Lcd.printf("Hello World!");
  pinMode(PIRPin, INPUT);
  Serial.begin(9600);
}

void loop()
{
  val = digitalRead(PIRPin);
  if (val == HIGH)   //si está activado
  {
    if (pirState == LOW)  //si previamente estaba apagado
    {
      Serial.println("Sensor activado");
      pirState = HIGH;
    }
  }
  else   //si esta desactivado
  {
    if (pirState == HIGH)  //si previamente estaba encendido
    {
      Serial.println("Sensor parado");
      pirState = LOW;
    }
  }
}
