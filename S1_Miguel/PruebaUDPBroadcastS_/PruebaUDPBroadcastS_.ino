#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>
#include <TimeLib.h>

#define Echo 33
#define Trigger 34

const char * ssid = "Grupo9";
const char * password = "123456789";
AsyncUDP udp;
StaticJsonDocument<200> jsonBuffer; //tamaño maximo de los datos
const int PIRPin = 5;         // pin de entrada (for PIR sensor)
int pirState = LOW;           // de inicio no hay movimiento
int val = 0;

void setup() {

  Serial.begin(115200);
  pinMode(Echo,INPUT);
  pinMode(Trigger, OUTPUT);
  pinMode(PIRPin, INPUT);
  setTime (9, 15, 0, 7, 10, 2019); //hora minuto segundo dia mes año
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WiFi Failed");
    while (1) {
      delay(1000);
    }
  }
  
  if (udp.listen(1234)) {
    Serial.print("UDP Listening on IP: ");
    Serial.println(WiFi.localIP());
    udp.onPacket([](AsyncUDPPacket packet) {
      Serial.write(packet.data(), packet.length());
      Serial.println();
    });
  }
  
}

void loop() {


  float distancia;
  bool presencia;
  distancia = Distancia();
  presencia = Presencia();
  EnviarDatos(distancia, presencia);
  delay(5000);

}

float Distancia() {
  long duracion, distanciaCm;
  digitalWrite(Trigger, LOW);
  delayMicroseconds(4);
  digitalWrite(Trigger, HIGH);
  delayMicroseconds(10);
  digitalWrite(Trigger, LOW);
  duracion = pulseIn(Echo, HIGH);
  delay(100);
  Serial.println(duracion);
  distanciaCm = duracion * 10 / 292 / 2;
  Serial.println(distanciaCm);
  return distanciaCm;
}

bool Presencia() {
  val = digitalRead(PIRPin);
  Serial.println("Presencia " + val);
  if (val == HIGH)   //si está activado
  {
    if (pirState == LOW)  //si previamente estaba apagado
    {
      Serial.println("Sensor activado");
      pirState = HIGH;
      return true;
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
  return false;
}

void EnviarDatos(float distancia, bool presencia){

delay(1000);
char texto[200];
jsonBuffer["Hora"]=hour(); //Datos introducidos en el objeto “jsonbuffer"
jsonBuffer["Minuto"]=minute(); //3 campos
jsonBuffer["Segundo"]=second(); //
jsonBuffer["Distancia"]=distancia;
jsonBuffer["Presencia"]= presencia;
serializeJson(jsonBuffer, texto); //paso del objeto “jsonbuffer" a texto para
//transmitirlo
udp.broadcastTo(texto,1234); //se envía por el puerto 1234 el JSON
//como texto

}
