//declare two int's with pin values
int pinOne = A0;
int pinTwo = A1;

// the setup routine runs once when you press reset:
void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
  pinMode(pinOne, INPUT);
  pinMode(pinTwo, INPUT);
}

// the loop routine runs over and over again forever:
void loop() {
  // read the input on analog pin 0(pinOne) and one(pintTwo):
  int sensorValueOne = analogRead(pinOne);
  int sensorValueTwo = analogRead(pinTwo);

  //declare char array text and store values
  char text[40];
  sprintf(text, "%d,%d\n", sensorValueOne, sensorValueTwo);
  //print text array
  Serial.println(text);
  delay(10);
}
