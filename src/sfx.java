
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

import lejos.nxt.Sound; 


public class sfx {

	public static void main(String[] args) {
		
	}
	
	/**
	 * Let the robot beep
	 */
	public void beep() {
		Sound.beep();
	}
	
	/**
	 * Let the robot beep x times
	 * 
	 * @param times how often the robot should beep
	 */
	public void beep(int times) {
		while (times > 0) {
			beep();
			times--;
		}
	}

}
