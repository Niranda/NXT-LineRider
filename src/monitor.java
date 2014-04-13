
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

import lejos.nxt.LCD; 
import lejos.nxt.Button;

/*
 * Description
 * 	Bereitstellung grundlegender Ausgabefunktion über das LCDisplay
 * 
 * Display-Resolution:
 *  100x64
 *  
 * Possible Chars:
 * 	17x8
 */

public class monitor {
	/* *****************************
	 * VARIABLE DECLARATION
	 * *****************************/
	

	/* *****************************
	 * METHODS
	 * *****************************/
	public static void main(String[] args)throws Exception {
		drawBox(50, 30, 10, 10);
		
		Button.ENTER.waitForPressAndRelease();
	}
	
	
	/**
	 * Draws a simple box, starting in the top left corner up to the buttom right corner
	 * 
	 * @param x width in pixel
	 * @param y height in pixel
	 * @param xStart x-coord of start-point
	 * @param yStart y-coord of start-point 
	 */
	public static void drawBox(int x, int y, int xStart, int yStart) {
		for (int i=0; i<100; i++) {
			
			if (i < x) {																			// horizontal
				LCD.setPixel(xStart + i, yStart, 1);
				LCD.setPixel(xStart + i, yStart + y, 1);
			}
			
			if (i < y) {																			// vertical
				LCD.setPixel(xStart, yStart + i, 1);
				LCD.setPixel(xStart + x, yStart + i, 1);
			}
		}
	}
	
	
	/**
	 * Draws an arrow in any angel.
	 * 
	 * @param xStart x-coord of the arrow's peek
	 * @param yStart y-coord of the arrow's peek
	 * @param xEnd x-coord of the arrow's end
	 * @param yEnd y-coord of the arrow's end
	 */
	public static void drawArrow(int xStart, int yStart, int xEnd, int yEnd) {
		// TODO, sry :D
	}
}
