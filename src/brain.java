
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

/*
 * Description
 * 	
 */

public class brain {
	/* *****************************
	 * VARIABLE DECLARATION
	 * *****************************/
	
	/*
	 * OBJECTS
	 */
	private static dataExchange de;
	private static movingSensor sensor;
	private static engine engine;
//	private monitor monitor = new monitor();
	
	
	
	/* *****************************
	 * METHODS
	 * *****************************/
	public static void main(String[] args)throws Exception {
		de = new dataExchange();
		sensor = new movingSensor(de);
		engine = new engine(de);
		engine.setSpeed(5);
		sensor.setSpeed(150);
		
		sensor.start();
		engine.start();
		
		while(!Button.ESCAPE.isDown()) {
			// let it sno... work, yeah, let it work!
		}
		
		LCD.drawString("FINISHED", 0, 7);
		LCD.refresh();
		
		System.exit(0);
	}

}
