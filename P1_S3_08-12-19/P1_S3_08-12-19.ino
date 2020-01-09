#include <TimeLib.h>
#include <Wire.h>
#include <PubSubClient.h>
#include <WiFi.h>

const int PinM1 = 16;//derecha positivo
const int PinM2 = 0;//derecha negativo
const int PinM3 = 4;//izquierda negativo
const int PinM4 = 17;//izquierda positivo
const int Echo = 18;
const int Trigger = 25;
const int F1 = 34;
const int F2 = 35;
const char * ssid = "Grupo9";
const char * password = "123456789";
const char* mqtt_server = "test.mosquitto.org";
//const char* mqtt_server = "broker.mqtt-dashboard.com";
WiFiClient espClient;
PubSubClient client(espClient);
int c1, c2;
int hora;
boolean rec = 0;
int pos = 0;    // posicion del servo
long lastMsg = 0;
char msg[50];
int value = 0;
String state;
volatile bool stateChange = false;
hw_timer_t * timer = NULL;


void ISR_TIMER() {
  
}

void ISR_F1() {
  int value = analogRead(F1);
  c1++;
  Serial.println(c1);
  Serial.println(value);
}
void ISR_F2() {
  int value = analogRead(F2);
  c2++;
  Serial.println(c2);
  Serial.println(value);
}

void Idle_task() {


}

void modoAutomatico() {

  avanzar();

}

void calibrarDistancia() {


}

void modoPerimetro() {

}

void modoManual() {

}

void modoAmenaza() {

}

void modoRecargaSolar() {

}

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;

  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

  if (messageTemp == "0x0000") {
    stateChange = true;
    Serial.print("prueba");
    Serial.println(messageTemp);
    Serial.print(stateChange);
    state = "0x0000";
  }
  if (messageTemp == "0x0001") {
    stateChange = true;
    Serial.print("modoAutomatico");
    Serial.println(messageTemp);
    Serial.print(stateChange);
    state = "0x0001";
  }
  if (messageTemp == "0x0002") {
    stateChange = true;
    Serial.print("modoPerimetro");
    Serial.println(messageTemp);
    state = "0x0002";
  }
  if (messageTemp == "0x0003") {
    stateChange = true;
    Serial.print("modoManual");
    Serial.println(messageTemp);
    state = "0x0003";
  }
  if (messageTemp == "0x0004") {
    stateChange = true;
    Serial.print("modoAmenaza");
    Serial.println(messageTemp);
    state = "0x0004";
  }
  if (messageTemp == "0x0005") {
    stateChange = true;
    Serial.print("parar");
    Serial.println(messageTemp);
    state = "0x0005";
  }
  if (messageTemp == "0x0006") {
    stateChange = true;
    Serial.print("modoRecargaSolar");
    Serial.println(messageTemp);
    state = "0x0006";
  }
  if (messageTemp == "0x0007") {
    stateChange = true;
    Serial.print("modoRecargaSolar/avanzar");
    Serial.println(messageTemp);
    state = "0x0007";
  }
  if (messageTemp == "0x0008") {
    stateChange = true;
    Serial.print("modoRecargaSolar/derecha");
    Serial.println(messageTemp);
    state = "0x0008";
  }
  if (messageTemp == "0x0009") {
    stateChange = true;
    Serial.print("modoRecargaSolar/izquierda");
    Serial.println(messageTemp);
    state = "0x0009";
  }
  if (messageTemp == "0x000A") {
    stateChange = true;
    Serial.print("modoRecargaSolar/atras");
    Serial.println(messageTemp);
    state = "0x000A";
  }
  if (messageTemp == "0x000B") {
    stateChange = true;
    Serial.print("modoRecargaSolar/dDerecha");
    Serial.println(messageTemp);
    state = "0x000B";

  }
  if (messageTemp == "0x000C") {
    stateChange = true;
    Serial.print("modoManual/parar");
    Serial.println(messageTemp);
    state = "0x000C";

  }
  if (messageTemp == "0x000D") {
    stateChange = true;
    Serial.print("modoManual/avanzar");
    Serial.println(messageTemp);
    state = "0x000D";

  }
  if (messageTemp == "0x000E") {
    stateChange = true;
    Serial.print("modoManual/derecha");
    Serial.println(messageTemp);
    state = "0x000E";

  }
  if (messageTemp == "0x000F") {
    stateChange = true;
    Serial.print("modoManual/izquierda");
    Serial.println(messageTemp);
    state = "0x000F";

  }
  if (messageTemp == "0x0010") {
    stateChange = true;
    Serial.print("modoManual/atras");
    Serial.println(messageTemp);
    state = "0x0010";

  }

}



void setup() {

  Serial.begin(9600);
  Serial.println();
  pinMode(PinM1, OUTPUT);
  pinMode(PinM2, OUTPUT);
  pinMode(PinM3, OUTPUT);
  pinMode(PinM4, OUTPUT);
  pinMode(Echo, INPUT);
  pinMode(Trigger, OUTPUT);
  pinMode(F1, OUTPUT);
  pinMode(F2, OUTPUT);
  setTime (9, 15, 0, 15, 11, 2019); //hora minuto segundo dia mes año
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  timer = timerBegin(0, 80, true);
  timerAttachInterrupt(timer, &ISR_TIMER, true);
  timerAlarmWrite(timer, 1000000, true);
  timerAlarmDisable(timer);
  //timerAlarmEnable(timer);
  attachInterrupt(digitalPinToInterrupt(F1), ISR_F1, CHANGE);
  attachInterrupt(digitalPinToInterrupt(F2), ISR_F2, CHANGE);
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("robot/estado");

    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
  
void loop() {
  /*
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  
  if (stateChange == true) {
    timerAlarmEnable(timer);

  }
  
  if (stateChange == false) {
    timerAlarmDisable(timer);

  }
  
  if (stateChange == true) {
    Serial.println("adios");

    if (state == "0x0000") {
      stateChange = false;
      Idle_task();
    }
    if (state == "0x0001") {
      stateChange = false;
      modoAutomatico();
    }
    if (state == "0x0002") {
      stateChange = false;
      modoPerimetro();
    }
    if (state == "0x0003") {
      stateChange = false;
      modoManual();
    }
    if (state == "0x0004") {
      stateChange = false;
      modoAmenaza();
    }
    if (state == "0x0005") {
      stateChange = false;
      parar();
    }
    if (state == "0x0006") {
      stateChange = false;
      modoRecargaSolar();
    }
    if (state == "0x0007") {
      stateChange = false;
      avanzarRecarga();
    }
    if (state == "0x0008") {
      stateChange = false;
      derechaRecarga();
    }
    if (state == "0x0009") {
      stateChange = false;
      izquierdaRecarga();
    }
    if (state == "0x000A") {
      stateChange = false;
      atras();
    }
    if (state == "0x000B") {
      stateChange = false;
    }
    if (state == "0x000C") {
      stateChange = false;
      modoManualParar();
    }
    if (state == "0x000D") {
      stateChange = false;
      modoManualAvanzar();
    }
    if (state == "0x000E") {
      stateChange = false;
      modoManualDerecha();
    }
    if (state == "0x000F") {
      stateChange = false;
      modoManualIzquierda();
    }
    if (state == "0x0010") {
      stateChange = false;
      modoManualAtras();
    }
  }
 */
 modoAutomatico();
  delay(5);
}

void avanzar() {

  float distanciaAux;
  int aux;
  distanciaAux = distancia();
  String estado = "avanzar";
  //client.publish("modo", "0x1000");
  //transmitirEstado(distanciaAux, estado);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    //transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {
    atras();
  }
  Serial.println(distanciaAux);
  if (distanciaAux <= 100 && distanciaAux != 0 && distanciaAux > 20) {

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
    delay(360);


  }
}

void avanzarRecarga() {

  float distanciaAux;
  int aux;
  distanciaAux = distancia();
  String estado = "avanzar";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {

  }
  Serial.println(distanciaAux);
  if (distanciaAux <= 100 && distanciaAux != 0 && distanciaAux > 20) {



  } else {
    Serial.println("avanzar");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM4, HIGH);
    delay(360);


  }

}

void derecha() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "derecha";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {
    atras();
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
  if (distanciaAux <= 20) {

    atras();

  } if (distanciaAux <= 100 && distanciaAux != 0 && distanciaAux > 20) {

    Serial.println("giro");

    derecha();

  } else {

    avanzar();

  }

}

void derechaRecarga() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "derecha";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {

  } else {
    Serial.println("giro1");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM2, LOW);
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, LOW);
    delay(1000);

  }

}

void izquierda() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "Izquierda";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {
    atras();
  } if (distanciaAux <= 100 && distanciaAux != 0 && distanciaAux > 20) {
    Serial.println("giro1");
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, HIGH);
    digitalWrite(PinM1, LOW);
    digitalWrite(PinM2, LOW);
    delay(1000);
  }

  distanciaAux = distancia();
  Serial.println(distanciaAux);
  if (distanciaAux <= 20) {
    atras();
  } if (distanciaAux <= 100 && distanciaAux != 0 && distanciaAux > 20) {

    Serial.println("giro");

    izquierda();

  } else {

    avanzar();

  }

}

void izquierdaRecarga() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "Izquierda";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  Serial.println(distanciaAux);
  if (distanciaAux == 0) {

    String estado = "No funciona el sensor de ultrasonidos";
    transmitirEstado(distanciaAux, estado);

  }
  if (distanciaAux <= 20) {
  } else {
    Serial.println("giro1");
    digitalWrite(PinM3, LOW);
    digitalWrite(PinM4, HIGH);
    digitalWrite(PinM1, LOW);
    digitalWrite(PinM2, LOW);
    delay(1000);
  }
}

void parar() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "parar";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, LOW);

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

void transmitirEstado(float distancia, String estado) {

  char distanciaString[8];
  //    estado.toCharArray(char, 10);
  dtostrf(distancia, 1, 2, distanciaString);
  //client.publish("modo/", distanciaString);
  //    client.publish("esp32/state", estado);

}

void atras() {

  float distanciaAux;
  distanciaAux = distancia();
  String estado = "atras";
  client.publish("modo", "0x1000");
  transmitirEstado(distanciaAux, estado);
  Serial.println("atras");
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, HIGH);
  digitalWrite(PinM3, HIGH);
  digitalWrite(PinM4, LOW);
  delay(1000);
  avanzar();
}

void modoManualAvanzar() {
  digitalWrite(PinM1, HIGH);
  digitalWrite(PinM4, HIGH);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
}
void modoManualDerecha() {
  digitalWrite(PinM1, HIGH);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, LOW);
}

void modoManualIzquierda() {
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, HIGH);
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
}

void modoManualAtras() {
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, HIGH);
  digitalWrite(PinM3, HIGH);
  digitalWrite(PinM4, LOW);
}

void modoManualParar() {
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW);
  digitalWrite(PinM4, LOW);

}
