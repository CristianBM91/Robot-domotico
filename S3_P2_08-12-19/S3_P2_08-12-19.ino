#include <TimeLib.h>
#include <PubSubClient.h>
#include <M5Stack.h>
#include <WiFi.h>

const int PinFoco = 2;
const int AD1 = 36;
const int AD2 = 35;
const int AD3 = 17;
const int AD4 = 16;
const char * ssid = "Grupo9";
const char * password = "123456789";
const int PIRPin = 5;
const int speakerPin = 22;
const int beats[] = { 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 4 };
const int tempo = 300;
const char* mqtt_server = "broker.mqtt-dashboard.com";

WiFiClient espClient;
PubSubClient client(espClient);

volatile int state = LOW;
volatile int val = 0;

int hora;
boolean rec = 0;
int pirState = LOW;           // de inicio no hay movimiento
int length = 15; // the number of notes
String notes = "ccggaagffeeddc "; // a space represents a res
int pos = 0; // posicion del servo
char texto[200];
long lastMsg = 0;
char msg[50];
int value = 0;
String state_t;
hw_timer_t * timer = NULL;
volatile bool stateChange = false;


void ISR_TIMER() {

  
}

void Idle_task() {


}

void modoAutomatico() {


}

void calibrarDistancia() {


}

void modoPerimetro() {

}

void modoManual() {

}

void modoAmenaza() {

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
    state_t = "0x0000";
  }
  if (messageTemp == "0x0001") {
    stateChange = true;
    Serial.print("modoAutomatico");
    Serial.println(messageTemp);
    Serial.print(stateChange);
    state_t = "0x0001";
  }
  if (messageTemp == "0x0002") {
    stateChange = true;
    Serial.print("modoPerimetro");
    Serial.println(messageTemp);
    state_t = "0x0002";
  }
  if (messageTemp == "0x0003") {
    stateChange = true;
    Serial.print("modoManual");
    Serial.println(messageTemp);
    state_t = "0x0003";
  }
  if (messageTemp == "0x0004") {
    stateChange = true;
    Serial.print("modoAmenaza");
    Serial.println(messageTemp);
    state_t = "0x0004";
  }
  if (messageTemp == "0x0005") {
    stateChange = true;
    Serial.print("parar");
    Serial.println(messageTemp);
    state_t = "0x0005";
  }
  if (messageTemp == "0x0006") {
    stateChange = true;
    Serial.print("modoRecargaSolar");
    Serial.println(messageTemp);
    state_t = "0x0006";
  }
  if (messageTemp == "0x0007") {
    stateChange = true;
    Serial.print("modoRecargaSolar/avanzar");
    Serial.println(messageTemp);
    state_t = "0x0007";
  }
  if (messageTemp == "0x0008") {
    stateChange = true;
    Serial.print("modoRecargaSolar/derecha");
    Serial.println(messageTemp);
    state_t = "0x0008";
  }
  if (messageTemp == "0x0009") {
    stateChange = true;
    Serial.print("modoRecargaSolar/izquierda");
    Serial.println(messageTemp);
    state_t = "0x0009";
  }
  if (messageTemp == "0x000A") {
    stateChange = true;
    Serial.print("modoRecargaSolar/atras");
    Serial.println(messageTemp);
    state_t = "0x000A";
  }
  if (messageTemp == "0x000B") {
    stateChange = true;
    Serial.print("modoRecargaSolar/dDerecha");
    Serial.println(messageTemp);
    state_t = "0x000B";

  }
  if (messageTemp == "0x000C") {
    stateChange = true;
    Serial.print("modoManual/parar");
    Serial.println(messageTemp);
    state_t = "0x000C";

  }
  if (messageTemp == "0x000D") {
    stateChange = true;
    Serial.print("modoManual/avanzar");
    Serial.println(messageTemp);
    state_t = "0x000D";

  }
  if (messageTemp == "0x000E") {
    stateChange = true;
    Serial.print("modoManual/derecha");
    Serial.println(messageTemp);
    state_t = "0x000E";

  }
  if (messageTemp == "0x000F") {
    stateChange = true;
    Serial.print("modoManual/izquierda");
    Serial.println(messageTemp);
    state_t = "0x000F";

  }
  if (messageTemp == "0x0010") {
    stateChange = true;
    Serial.print("modoManual/atras");
    Serial.println(messageTemp);
    state_t = "0x0010";

  }
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

void setup() {

  Serial.println();
  pinMode(AD1, INPUT);
  pinMode(AD2, INPUT);
  pinMode(AD3, INPUT);
  pinMode(AD4, INPUT);
  pinMode(speakerPin, OUTPUT);
  pinMode(PinFoco, OUTPUT);
  pinMode(PIRPin, INPUT);
  setTime (9, 15, 0, 15, 11, 2019); //hora minuto segundo dia mes año
  M5.begin();
  Serial.begin(115200);
  setup_wifi();
  attachInterrupt(digitalPinToInterrupt(PIRPin), blink, CHANGE);
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);

}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("prueba");
      client.subscribe("modo");

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

  if (!client.connected()) {
    reconnect();
  }
  client.loop();
if (state_t == "0x0000") {
    Idle_task();
  }
  if (state_t == "0x0001") {
    modoAutomatico();
  }
  if (state_t == "0x0002") {
    modoPerimetro();
  }
  if (state_t == "0x0003") {
    modoManual();
  }
  if (state_t == "0x0004") {
    modoAmenaza();
  }
  if (state_t == "0x0005") {
  }
  if (state_t == "0x0006") {
  }
  if (state_t == "0x0007") {
  }
  if (state_t == "0x0008") {
  }
  if (state_t == "0x0009") {
  }
  if (state_t == "0x000A") {
  }
  if (state_t == "0x000B") {
  }
  if (state_t == "0x000C") {
  }
  if (state_t == "0x000D") {
  }
  if (state_t == "0x000E") {
  }
  if (state_t == "0x000F") {
  }
  if (state_t == "0x0010") {
  }
}

int luminosidad1 () {
  float Raw = 0;
  Raw = analogRead(AD1);
  float valor;

  if (Raw >= 3000) {

    valor = 100;
    return valor;

  } else {

    return Raw * 100 / 3000;

  }

}

int luminosidad2 () {

  float Raw = 0;
  Raw = analogRead(AD2);
  float valor;
  if (Raw >= 3000) {
    valor = 100;
    return valor;

  } else {

    return Raw * 100 / 3000;

  }

}

int luminosidad3 () {
  float Raw = 0;
  Raw = digitalRead(AD3);
  return 0;
}

int luminosidad4 () {

  float Raw = 0;
  Raw = digitalRead(AD4);
  return 0;

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

  int aux;
  aux = luminosidad1();
  if (aux <= 50) {

    controlFocoOn();

  } else {

    controlFocoOff();

  }

}

void transmitirEstadoUart() {

  //client.publish("M5Stack/presencia", state);
}

void blink() {

  val = digitalRead(PIRPin);
  if (val == HIGH)   //si está activado
  {
    //transmitirEstadoUart();
    Serial.println("Sensor activado");
    //client.publish("modo", "0x0001");
    state = !state;
    Serial.println(luminosidad1());
    Serial.println(luminosidad2());
    Serial.println(luminosidad3());
    Serial.println(luminosidad4());
  }
  else   //si esta desactivado
  {
    transmitirEstadoUart();
    Serial.println("Sensor parado");
    state = !state;
    Serial.println(luminosidad1());
    Serial.println(luminosidad2());
    Serial.println(luminosidad3());
    Serial.println(luminosidad4());

  }
}


void modoRecargaSolar() {

  for (;;) {
    int l1, l2, l3, l4;
    int LMax = 0;
    l1 = luminosidad1();
    l2 = luminosidad2();
    l3 = luminosidad3();
    l4 = luminosidad4();

    if (l1 > l2 && l1 > l3 && l1 > l4) {

      client.publish("modo", "0x0008");

    }

    if (l2 > l1 && l2 > l3 && l2 > l4) {

      client.publish("modo", "0x0009");


    }

    if (l3 > l2 && l3 > l1 && l3 > l4) {
      client.publish("modo", "0x0005");

    }

    if (l4 > l2 && l4 > l3 && l4 > l1) {
      client.publish("modo", "0x0005");


    }

    if (l2 == l1 && l2 < l3 && l2 < l4) {
      client.publish("modo", "0x0007");


    }

    if (l3 == l4 && l3 < l2 && l3 < l1) {
      client.publish("modo", "0x000A");


    } else {

      client.publish("modo", "0x0005");

    }
  }
}
