import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.*;
import grovepi.PinMode;

class Cube {
    
    public static void main(String[] args) {
        GrovePi grovePi = new GrovePi();
        grovePi.pinMode(Pin.DIGITAL_PIN_3, PinMode.OUTPUT); // Buzzer
        grovePi.pinMode(Pin.DIGITAL_PIN_5, PinMode.OUTPUT); // LED
        grovePi.pinMode(Pin.ANALOG_PIN_0, PinMode.INPUT); 	// Sound Sensor
        grovePi.pinMode(Pin.ANALOG_PIN_1, PinMode.INPUT);	// Light Sensor
        UltrasonicRangerSensor rangeSensor = grovePi.getDeviceFactory().createUltraSonicSensor(Pin.DIGITAL_PIN_4); // Proximity Sensor
        
        int buzzerVal = 0; // Val to write to buzzer
        int lightVal = 0; // Val to write to LED
        while(true) {
            System.out.println(rangeSensor.getDistance());
			//If a cube is in proximity
            if(rangeSensor.getDistance() < 20) {
				// Set the buzzer volume according to how close the cube is
                buzzerVal = 20 - rangeSensor.getDistance(); 
                grovePi.analogWrite(Pin.DIGITAL_PIN_3, buzzerVal);
				
				// Set the LED brightness according to how close the cube is
				lightVal = 100 / rangeSensor.getDistance(); 
                grovePi.analogWrite(Pin.DIGITAL_PIN_5, lightVal);
				
				// If the environmental noise is above a level, increase the buzzer volume further
                if(grovePi.analogRead(Pin.ANALOG_PIN_0) > 800) {
                    grovePi.analogWrite(Pin.DIGITAL_PIN_3, buzzerVal + 10);
                }
				// If the environmental light is below a level, increase the brightness of the LED
                if(grovePi.analogRead(Pin.ANALOG_PIN_1) < 5) {
                    grovePi.analogWrite(Pin.DIGITAL_PIN_5, lightVal + 100);
                }
            } else {
				// If no cube is in proximity, turn everything off
                buzzerVal = 0;
				lightVal = 0;
                grovePi.analogWrite(Pin.DIGITAL_PIN_3, buzzerVal);
                grovePi.analogWrite(Pin.DIGITAL_PIN_5, lightVal);
            }
            grovepi.common.Delay.milliseconds(500);
        }
    }
}
