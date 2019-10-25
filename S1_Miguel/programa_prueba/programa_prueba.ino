#define PinM1 2
#define PinM2 3
#define PinM3 4
#define PinM4 5
#define Echo 6
#define Trigger 7
#define ENA A5
#define ENB A3
#define PIRPin 8

int pirState = LOW;          
int val = 0;

void setup() {

  Serial.begin(9600);
  pinMode(PinM1, OUTPUT);
  pinMode(PinM2, OUTPUT);
  pinMode(PinM3, OUTPUT);
  pinMode(PinM4, OUTPUT);
  pinMode(Echo, INPUT);
  pinMode(Trigger, OUTPUT);
  pinMode(PIRPin, INPUT);

  
}

void loop() {
  
Automatico();

}

int Automatico(){

  float distancia;
  int aux;
  distancia = Distancia();
  if(distancia<50){
    
  aux = random(1, 2);

  if(aux == 1){
    Derecha();
  }
  if(aux == 2){
    Izquierda();  
  }
    return 0;
  }else{
    
  digitalWrite(PinM1, HIGH);
  digitalWrite(PinM2, HIGH);
  digitalWrite(PinM3, HIGH); 
  digitalWrite(PinM4, HIGH); 
  delay(5000);
    return 0;
  }
  }
  
void Derecha(){
  digitalWrite(PinM1, HIGH); 
  digitalWrite(PinM2, HIGH);
  digitalWrite(PinM3, LOW); 
  digitalWrite(PinM4, LOW);
  delay(5000);
  }
void Izquierda(){
  digitalWrite(PinM3, HIGH); 
  digitalWrite(PinM4, HIGH);
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  delay(5000);
  }
void Parar(){
  digitalWrite(PinM1, LOW);
  digitalWrite(PinM2, LOW);
  digitalWrite(PinM3, LOW); 
  digitalWrite(PinM4, LOW);
  }
float Distancia(){
  long duracion, distanciaCm;
 digitalWrite(Trigger, LOW); 
 delayMicroseconds(4);
 digitalWrite(Trigger, HIGH); 
 delayMicroseconds(10);
 digitalWrite(Trigger, LOW);
 duracion = pulseIn(Echo, HIGH); 
 distanciaCm = duracion * 10 / 292 / 2; 
 Serial.println(distanciaCm);
 return distanciaCm;
  }
void Movimiento(){

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
  
