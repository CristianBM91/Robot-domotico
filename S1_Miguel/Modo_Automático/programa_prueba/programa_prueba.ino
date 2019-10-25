#define PinM1 2
#define PinM2 3
#define PinM3 4
#define PinM4 5
#define Echo 10
#define Trigger 11
#define ENA A5
#define ENB A3
void setup() {

  Serial.begin(9600);
  pinMode(OUTPUT, PinM1);
  pinMode(OUTPUT, PinM2);
  pinMode(OUTPUT, PinM3);
  pinMode(OUTPUT, PinM4);
  pinMode(INPUT, Echo);
  pinMode(OUTPUT, Trigger);
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
  if (distancia < 50) {

    aux = random(1, 4);
    Serial.println("giro");
    if (aux == 2 || 1) {
      Derecha();
    }
    if (aux == 3 || 4) {
      Izquierda();
    }

  } else {
    Serial.println("adelante");
    digitalWrite(PinM1, HIGH);
    digitalWrite(PinM2, HIGH);
    digitalWrite(PinM3, HIGH);
    digitalWrite(PinM4, HIGH);
    delay(500);


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
  } if(distancia < 50) {

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
  } if (distancia < 50) {
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
  } if(distancia < 50) {

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
  delay(1000);
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
