import lejos.nxt.*;

public class teachSensor {
	private static LightSensor sensor = new LightSensor(SensorPort.S1);
		
	public static void main(String[] args) throws InterruptedException {
		
		while (!Button.ESCAPE.isDown()) {
			LCD.clear();
			LCD.drawInt(sensor.readNormalizedValue(), 3, 4);
			
			LCD.drawString("#        |", 0, 2);
			Thread.sleep(1000);
			
			LCD.drawString("# #      |", 0, 2);
			Thread.sleep(1000);
			
			LCD.drawString("# # #    |", 0, 2);
			Thread.sleep(1000);
			
			LCD.drawString("# # # #  |", 0, 2);
			Thread.sleep(1000);
			
			LCD.drawString("# # # # #|", 0, 2);
			Thread.sleep(1000);
		}
	}

}
