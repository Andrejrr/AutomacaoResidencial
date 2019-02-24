/*
  Serial Event example

  When new serial data arrives, this sketch adds it to a String.
  When a newline is received, the loop prints the string and clears it.

  A good test for this is to try it with a GPS receiver that sends out
  NMEA 0183 sentences.

  NOTE: The serialEvent() feature is not available on the Leonardo, Micro, or
  other ATmega32U4 based boards.

  created 9 May 2011
  by Tom Igoe

  This example code is in the public domain.

  http://www.arduino.cc/en/Tutorial/SerialEvent
*/

String inputString = "";         // a String to hold incoming data
long tempo;
bool statusSaidas[20];
void setup() {
  // initialize serial:
  Serial.begin(115200);
  Serial1.begin(115200);
  //mapeia saidas
  for(int i = 33;i<53;i++){
    pinMode(i, OUTPUT);
  }
  tempo = millis();
}

void loop() {
  if(millis()-tempo>500){
    for(int i = 33;i<53;i++){
      if(statusSaidas[i-33])
        digitalWrite(i, HIGH);
      else
        digitalWrite(i, LOW);
    }
    tempo = millis();
  Serial1.println(saidasToString());
  Serial.println(saidasToString());
  Serial.println(inputString);
  }

}

/*
  SerialEvent occurs whenever a new data comes in the hardware serial RX. This
  routine is run between each time loop() runs, so using delay inside loop can
  delay response. Multiple bytes of data may be available.
*/
void serialEvent1() {
  Serial.println("evento serial");
  String entradaSerial;
  while (Serial1.available()) {
    // get the new byte:
    char inChar = (char)Serial1.read();
    // add it to the inputString:
    entradaSerial += inChar;
    Serial.println(entradaSerial);
  }
  Serial.println(entradaSerial);
  inputString = entradaSerial;
  trataSerial(entradaSerial);
  
 
}

void trataSerial(String entradaSerial){
  if(entradaSerial.startsWith("modificarSaida")){
    int i = entradaSerial.indexOf(":")+1;
    entradaSerial.remove(0,i);
    statusSaidas[entradaSerial.toInt()] = !statusSaidas[entradaSerial.toInt()];
    Serial1.println("recebido ="+saidasToString());
  }
}

String saidasToString(){
  String s;
  for(int i = 0;i<20;i++){
    if(statusSaidas[i])
      s += '1';
    else
      s += '0';
  }
  return s;
}
