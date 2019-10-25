//#include "WiFi.h"
//#include "AsyncUDP.h"
//#include <ArduinoJson.h>
//#include <TimeLib.h>


#define PinM1 2
#define PinM2 3  
#define PinM3 4
#define PinM4 5 //funciona

#define Echo 13
#define Trigger 12
#define ENA A5
#define ENB A3

const char * ssid = "Grupo9";
const char * password = "123456789";
//AsyncUDP udp;
//StaticJsonDocument<200> jsonBuffer; //tamaño maximo de los datos
const int PIRPin = 13;         // pin de entrada (for PIR sensor)
int pirState = LOW;           // de inicio no hay movimiento
int val = 0;

void setup() {

  Serial.begin(9600);
  pinMode(OUTPUT, PinM1);
  pinMode(OUTPUT, PinM2);
  pinMode(OUTPUT, PinM3);
  pinMode(OUTPUT, PinM4);
  pinMode(INPUT, Echo);
  pinMode(OUTPUT, Trigger);
  pinMode(INPUT, PIRPin);
  /*
  setTime (9, 15, 0, 7, 10, 2018); //hora minuto segundo dia mes año
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
  */
}

void loop() {

  Delante();

}

void Delante() {

  Parar();
  float distancia;
  int aux;
  distancia = Distancia();
  if (distancia == 0) {
    Parar();
  }
  Serial.println(distancia);
  if (distancia < 50 && distancia!= 0) {

    aux = random(1, 4);
    
    if (aux == 2 || 1) {
      Serial.println("giroDerecha");
      Derecha();
    }
    if (aux == 3 || 4) {
      Serial.println("giroIzquierda");
      Izquierda();
    }

  } else {
    Serial.println("adelante");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM2, HIGH);
    digitalWrite(PinM3, HIGH);
    digitalWrite(PinM4, HIGH);
    delay(750);


  }

}

void Derecha() {

  float distancia;
  distancia = Distancia();
  Serial.println(distancia);
  Parar();
  if (distancia == 0) {
    Parar();
  } else {
    Serial.println("giro1");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM2, HIGH);
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, LOW);
    delay(1000);

  }

  distancia = Distancia();
  Serial.println(distancia);
  if (distancia == 0) {
    Parar();
  } if (distancia < 50 && distancia!= 0) {

    Serial.println("giro");

    Derecha();

  } else {

    Delante();

  }



}
void Izquierda() {

  float distancia;
  distancia = Distancia();
  Serial.println(distancia);
  Parar();
  if (distancia == 0) {
    Parar();
  } if (distancia < 50 && distancia!= 0) {
    Serial.println("giro1");
    digitalWrite(PinM3, HIGH);
    digitalWrite(PinM4, HIGH);
    digitalWrite(PinM1, LOW);
    digitalWrite(PinM2, LOW);
    delay(1000);
  }

  distancia = Distancia();
  Serial.println(distancia);
  if (distancia == 0) {
    Parar();
  } if (distancia < 50 && distancia!= 0) {

    Serial.println("giro");

    Izquierda();

  } else {

    Delante();

  }


}

void Parar() {
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, LOW);
  delay(500);
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

void presencia() {
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

void EnviarDistancia(float distancia){

delay(1000);
char texto[200];
//jsonBuffer["Hora"]=hour(); //Datos introducidos en el objeto “jsonbuffer"
//jsonBuffer["Minuto"]=minute(); //3 campos
//jsonBuffer["Segundo"]=second(); //
//jsonBuffer["Distancia"]=distancia;
//serializeJson(jsonBuffer, texto); //paso del objeto “jsonbuffer" a texto para
//transmitirlo
//udp.broadcastTo(texto,1234); //se envía por el puerto 1234 el JSON
//como texto
}
