
/*
 * Author:
 * 	Arne 'Niranda' Wandt
 * 	http://niranda.net
 * 
 * Project
 * 	NiraBot - NXT LineRider
 * 	(Mobile Agenten [HS-Wismar])
 * 
 * Date
 * 	Early 2014
 * 
 * License
 * 	GNU GPL v3 excluding a commercial/profit-oriented use
 */

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

/*
 * Description
 * 	
 */


public class LineRider {
	static DifferentialPilot robot = new DifferentialPilot(5.6f,13.0f,Motor.A, Motor.C); 	// Pilot-Objekt Erzeugung (Raddurchmesser, Abstand, Motor1, Motor2)

	public static void main(String[] args)throws Exception { 
		Button.ENTER.waitForPressAndRelease();												// Erst starten, wenn Enter gedrückt..

		final LightSensor ls = new LightSensor(SensorPort.S1);								// Erzeugung Lichtsensors am Port S1 
		int lange = 2;																		// Drehinterwall 
		int gesamt = 0;																		// gesamter Drehinterwall 
		int i = 1;																			// Varible zum bestimmen ob recht oder links drehen um nach der Linie zu suchen 

		robot.setTravelSpeed(400); 															// Geschwindigkeit setzen

		
		while (!Button.ESCAPE.isDown()) { 													// Wdh. solange Esc. nicht gedrückt wurde
			ausgabe(ls.readNormalizedValue());																// LCD Ausgabe des gesamten Drehinterwalls
/*
			if (ls.readNormalizedValue() <= 400) {											// Sensorwert <=400 -> Schwarz
				robot.forward(); 
				gesamt = 0;																	// gesamt wird auf 0 zurückgesetzt 
			} 

			else if (ls.readNormalizedValue() > 400) {										// Sensorwert >400 -> Weiß
				if (i == 0) {																	// suche rechts nach der Linie  
					robot.rotate(lange);													// Drehen im Interwall nach rechts 
					gesamt= gesamt + lange;													// gesamt um ein Interwall erhöhen
					
					if (gesamt >= 30) {														// Wenn gesamter Suchinterwall größer als 30...
						robot.rotate(-gesamt+10); 											// ...zurückdrehen
						
						i=1; 																// links suchen
						gesamt =0; 															// gesamt zurücksetzen
					}
				}
				
				if ( i==1 ) {																// Suche Rechts nach der Linie 
					robot.rotate(-lange);													// Drehen im Interwall nach links 
					gesamt= gesamt + lange;													// gesamt um ein Interwall erhöhen 
 
					if (gesamt >= 30) {														// Wenn gesamter Suchinterwall größer als 30...
						robot.rotate(gesamt-10); 											// zurückdrehen

						i = 0; 																// rechts suchen
						gesamt = 0; 														// gesamt wird auf 0 zurückgesetzt 
					} 
				} 
			} 
			*/
		} 
	}

	public static void ausgabe(int gesamt) {												// Ausgabemethode auf dem LCD-Display 
		LCD.clear(); 
		LCD.drawInt(gesamt, 3, 1);

		
//		LCD.drawString("Gesamt",3,1); 
//		LCD.drawInt((int)gesamt,7,2); 
//		LCD.drawString("Speed",8,4); 
//		LCD.drawInt((int)robot.getRobotSpeed(),8,5); 
//		LCD.refresh(); 
	}
}
