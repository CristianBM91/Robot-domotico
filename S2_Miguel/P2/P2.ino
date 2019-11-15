#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>
#include <TimeLib.h>
//#include <Servo.h>
#include <M5Stack.h>

String mac;
String nombre;
String modo;
String fecha;
int PinFoco = 21;
int AD1 = 23;
int AD2 = 19;
int AD3 = 18;
int AD4 = 22;
const char * ssid = "Grupo9";
const char * password = "123456789";
AsyncUDP udp;
StaticJsonDocument<200> jsonBuffer; //tamaño maximo de los datos
int hora;
boolean rec = 0;
const int PIRPin = 2;         // pin de entrada (for PIR sensor)
int pirState = LOW;           // de inicio no hay movimiento
int val = 0;
int speakerPin = 5;
int length = 15; // the number of notes
String notes = "ccggaagffeeddc "; // a space represents a rest
int beats[] = { 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 4 };
int tempo = 300;
//Servo myservo;  // crea el objeto servo
int pos = 0; // posicion del servo
char texto[200];

void setup() {

  pinMode(AD1, INPUT);
  pinMode(AD2, INPUT);
  pinMode(AD3, INPUT);
  pinMode(AD4, INPUT);
  pinMode(speakerPin, OUTPUT);
  pinMode(PinFoco, OUTPUT);
  pinMode(PIRPin, INPUT);
  //myservo.attach(9);
  setTime (9, 15, 0, 15, 11, 2019); //hora minuto segundo dia mes año
  M5.begin();
  Serial.begin(115200);
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
      int i = 200;
      while (i--) {
        *(texto + i) = *(packet.data() + i);
      }
      rec = 1; //recepcion de un mensaje
    });
  }

}

void loop() {

  bool presenciaAux;
  int luminosidadAux;
  //presenciaAux = presencia();
//  Serial.println(presenciaAux);
  //luminosidadAux = luminosidad1();
 // Serial.println(luminosidadAux);
  transmitirEstadoUart();
  alarma();

}

bool presencia() {

Serial.println("HOLA");
  val = digitalRead(PIRPin);
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

int luminosidad1 () {

  int valor = digitalRead(AD1);

  return valor;

}

int luminosidad2 () {

  int valor = digitalRead(AD2);

  return valor;

}

int luminosidad3 () {

  int valor = digitalRead(AD3);

  return valor;

}

int luminosidad4 () {

  int valor = digitalRead(AD4);

  return valor;

}

void playTone(int tone, int duration) {
  for (long i = 0; i < duration * 1000L; i += tone * 2) {
    digitalWrite(speakerPin, HIGH);
    delayMicroseconds(tone);
    digitalWrite(speakerPin, LOW);
    delayMicroseconds(tone);
  }
}

void playNote(char note, int duration) {
  char names[] = { 'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C' };
  int tones[] = { 1915, 1700, 1519, 1432, 1275, 1136, 1014, 956 };

  // play the tone corresponding to the note name
  for (int i = 0; i < 8; i++) {
    if (names[i] == note) {
      playTone(tones[i], duration);
    }
  }
}

void alarma() {

  for (int i = 0; i < length; i++) {
    if (notes[i] == ' ') {
      delay(beats[i] * tempo); // rest
    } else {
      playNote(notes[i], beats[i] * tempo);
    }

    // pause between notes
    delay(tempo / 2);
  }
}

void controlFocoOn() {

  digitalWrite(PinFoco, HIGH);
}

void controlFocoOff() {

  digitalWrite(PinFoco, LOW);
}

void controlFoco() {

  for (;;) {


    int luminosidadAux = luminosidad1();
    int valor = 30;

    if (luminosidadAux <= valor) {
      controlFocoOn();
    } else {
      controlFocoOff();
    }

  }

}

void controlCamaraVertical(int giroV) {

  //myservo.write(0);
  delay(15);
  //myservo.write(giroV);

}

void controlCamaraHorizontal(int giroH) {

  //myservo.write(0);
  delay(15);
  //myservo.write(giroH);

}

void transmitirEstadoUart() {

  if (rec) {

    if (Serial.available() > 0){
    char command = (char) Serial.read();
    switch (command){
      case 'H':
      Serial.println("Hola Mundo");
      break;
      case 'D':
      rec = 0;
    udp.broadcastTo("Recibido", 1234); //Confirmación
    udp.broadcastTo(texto, 1234); //reenvía lo recibido
    hora = atol(texto); //paso de texto a int
    StaticJsonDocument<200> jsonBufferRecv; //definición buffer para almacenar el objeto JSON, 200 máximo
    DeserializationError error = deserializeJson(jsonBufferRecv, texto); //paso de texto a formato JSON
    if (error)
      return;
    //    jsonBufferRecv = jsonBufferRecv + presencia + luminosidad;
    serializeJson(jsonBufferRecv, Serial); //envío por el puerto serie el objeto "jsonBufferRecv"
    Serial.println(); //nueva línea
      break;
    }
    

  }

}
}
