package cz.fi.muni.ia158.messenger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Messenger {
	
	private BlockingQueue<String> messages = new LinkedBlockingQueue<>();
	
	public Messenger(){
		new Thread(new MovementControl(messages)).start();
		Thread touchSensorControl = new Thread(new TouchSensorControl(messages));
		touchSensorControl.setPriority(Thread.MAX_PRIORITY);
		touchSensorControl.start();
	}

	public static void main(String[] args) throws InterruptedException {
		new Messenger();
	}

	private static void touchSensor() {
		Port port = LocalEV3.get().getPort("S1");

		SensorMode rgbMode = new EV3TouchSensor(port).getTouchMode();
		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[rgbMode.sampleSize()];

		// fetch a sample
		int prevValue = 0;
		while(true){ 
		  rgbMode.fetchSample(sample, 0);
		  for(int i = 0; i < sample.length; i++){
			  if((int) sample[i] != prevValue){
				  prevValue = (int) sample[i];
				  System.out.println(prevValue);
			  }
		  }
		}
		
	}

	private static void colorSensor() {
		// get a port instance
		Port port = LocalEV3.get().getPort("S2");


		//SensorMode rgbMode = new EV3ColorSensor(port).getRGBMode();
		SensorMode rgbMode = new EV3ColorSensor(port).getRGBMode();
		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[rgbMode.sampleSize()];

		// fetch a sample
		while(true){ 
		  rgbMode.fetchSample(sample, 0);
		  System.out.println("RGB:");
		  for(int i = 0; i < sample.length; i++){
			  //System.out.println(convertPercentageToHex(sample[i]));
			  System.out.println(convertToColor(sample[i]));
		  }
		  Delay.msDelay(1000);
		}
	}

	private static String convertToColor(float f) {
		String[] colors = {"NONE", "BLACK", "BLUE", "GREEN", "YELLOW", "RED", "WHITE", "BROWN"};

		int n = (int) f;
		if (n >7 || n<0){
			return n+"";
		}

		return colors[n];
	}

	private static String convertPercentageToHex(float f) {
		String val = Integer.toHexString((int) (f*256));
		return val.length() == 1 ? "0" + val : val;
	}

}
