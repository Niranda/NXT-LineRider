
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

import lejos.nxt.*; 
import lejos.robotics.navigation.*;

/*
 * Description
 * 	Bereitstellung grundlegender Funktionen des bewegbaren Sensors in der Front des Roboters (180° schwenkbar)
 */

public class movingSensor {
	public static void main(String[] args)throws Exception  {
		while (!Button.ESCAPE.isDown()) {
			LCD.drawString("Posi 0", 1, 1);
			LCD.drawString("Enter!", 1, 2);
			
			if (Button.RIGHT.isDown()) {
				Motor.B.rotate(-5);
			}
			else if (Button.LEFT.isDown()) {
				Motor.B.rotate(5);
			}
			else if (Button.ENTER.isDown()) {
				Motor.B.resetTachoCount();
			}
		
			LCD.drawInt(Motor.C.getPosition(), 1, 3);
		}
	}

}
