#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>
#include <TimeLib.h>
//#include <Servo.h>

    String mac;
    String nombre;
    String modo;
    String fecha;
    int PinM1 = 2;//derecha positivo
    int PinM2 = 0;//derecha negativo
    int PinM3 = 4;//izquierda negativo
    int PinM4 = 5;//izquierda positivo
    int PinF1 = 33;
    int PinF2 = 26;
    int PinF3 = 25;
    int Echo = 14;
    int Trigger = 12;
    int c1;
    int c2;
    int c3;
    const char * ssid = "Grupo9";
    const char * password = "123456789";
    AsyncUDP udp;
    StaticJsonDocument<200> jsonBuffer; //tamaño maximo de los datos
    int hora;
    boolean rec = 0;
    //Servo myservo;  // crea el objeto servo
    int pos = 0;    // posicion del servo


void setup() {
  Serial.begin(9600);
  pinMode(PinM1, OUTPUT);
  pinMode(PinM2, OUTPUT);
  pinMode(PinM3, OUTPUT);
  pinMode(PinM4, OUTPUT);
  pinMode(Echo, INPUT);
  pinMode(Trigger, OUTPUT);
  pinMode(PinF1, INPUT);
  pinMode(PinF2, INPUT);
  pinMode(PinF3, INPUT);
  setTime (9, 15, 0, 15, 11, 2019); //hora minuto segundo dia mes año
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

  //deteccionColisiones();

}

void loop() {

  avanzar();
  atras();
  float distanciaAux;
  distanciaAux = distancia();
  transmitirEstado(distanciaAux);
  obtenerColisiones();
  
}

void avanzar() {

  float distanciaAux;
  int aux;
  distanciaAux = distancia();
  if (distanciaAux == 0) {
    parar();
  }
  Serial.println(distanciaAux);
  if (distanciaAux < 50 && distanciaAux != 0) {

    aux = random(1, 4);

    if (aux == 2 || 1) {
      Serial.println("giroDerecha");
      derecha();
    }
    if (aux == 3 || 4) {
      Serial.println("giroIzquierda");
      izquierda();
    }

  } else {
    Serial.println("avanzar");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM4, HIGH);
    delay(750);


  }

}

void derecha() {

  float distanciaAux;
  distanciaAux = distancia();
  Serial.println(distanciaAux);
  parar();
  if (distanciaAux == 0) {
    parar();
  } else {
    Serial.println("giro1");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM2, LOW);
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, LOW);
    delay(1000);

  }

  distanciaAux = distancia();
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {
    parar();
  } if (distanciaAux < 50 && distanciaAux != 0) {

    Serial.println("giro");

    derecha();

  } else {

    avanzar();

  }
}

void izquierda() {

  float distanciaAux;
  distanciaAux = distancia();
  Serial.println(distanciaAux);
  parar();
  if (distanciaAux == 0) {
    parar();
  } if (distanciaAux < 50 && distanciaAux != 0) {
    Serial.println("giro1");
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, HIGH);
    digitalWrite(PinM1, LOW);
    digitalWrite(PinM2, LOW);
    delay(1000);
  }

  distanciaAux = distancia();
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {
    parar();
  } if (distanciaAux < 50 && distanciaAux != 0) {

    Serial.println("giro");

    izquierda();

  } else {

    avanzar();

  }


}

void parar() {

  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, LOW);
  delay(500);

}

float distancia() {
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

void transmitirEstado(float distancia) {

  Serial.println("Envio");
  delay(1000);
  char texto[200];
  jsonBuffer["Hora"] = hour(); //Datos introducidos en el objeto “jsonbuffer"
  jsonBuffer["Minuto"] = minute(); //3 campos
  jsonBuffer["Segundo"] = second(); //
  jsonBuffer["Distancia"] = distancia;
  serializeJson(jsonBuffer, texto); //paso del objeto “jsonbuffer" a texto para
  //transmitirlo
  udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON
  //como texto

}

void atras() {

  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, HIGH);
  digitalWrite(PinM3, HIGH);
  digitalWrite(PinM4, LOW);
  delay(750);

}

void controlGiroDistancia(int giroU) {

  //myservo.write(0);
  delay(15);
  //myservo.write(giroU);

}

void deteccionColisiones() {

  int aux1;
  int aux2;
  int aux3;

  for (;;) {

    aux1 = digitalRead(PinF1);
    aux2 = digitalRead(PinF2);
    aux3 = digitalRead(PinF3);

    if (aux1 == HIGH) {

      c1++;
      Serial.println(c1);

    }
    if (aux2 == HIGH) {

      c2++;
      Serial.println(c2);


    }
    if (aux3 == HIGH) {

      c3++;
      Serial.println(c3);


    }

  }
}

String obtenerColisiones() {

  String aux = "funciona";
  Serial.println(aux);
 // String aux = c1 + " " + c2 + " "+ c3;

  return aux;

}
