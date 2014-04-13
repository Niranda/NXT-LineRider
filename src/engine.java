

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
 * 	Bereitstellung grundlegender Funktionen der Antriebsmotoren
 */

public class engine {
	static int speed;
	static DifferentialPilot robot = new DifferentialPilot(5.5, 10.5, Motor.A, Motor.C); 	// Pilot-Objekt Erzeugung (Raddurchmesser cm, Abstand cm, Motor1, Motor2)
	
	
	public static void main(String[] args)throws Exception {
		
	}
	
	public static void forward() {
		forward();
	}
	
	public static void setSpeed(int paraSpeed) {
		robot.setTravelSpeed(paraSpeed);
	}
	
	}
