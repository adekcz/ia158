package cz.fi.muni.ia158.messenger;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class TouchSensorSensor {

	
	public void run(){
		System.out.println("running");
		Port port = LocalEV3.get().getPort("S1");
		SensorMode rgbMode = new EV3TouchSensor(port).getTouchMode();
		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[rgbMode.sampleSize()];

		
		long lastTime = System.currentTimeMillis();
		// fetch a sample
		boolean pastValue = false;
		String currentLetter = "";
		while(true){ 
		  rgbMode.fetchSample(sample, 0);
		  boolean currentValue = sample[0] == 1;
		  long pressedDuration = System.currentTimeMillis() - lastTime;
		  if(pressedDuration>2000 && !currentLetter.isEmpty()){
			  System.out.println("char: " + MorseDecoder.decode(currentLetter));
			  currentLetter = "";
		  }
		  if( currentValue != pastValue ){
			  pastValue = currentValue;
			  lastTime = System.currentTimeMillis();
			  if(currentValue == false){
				  System.out.println("pd: " + pressedDuration + " " + currentLetter);
				  currentLetter += encodeDuration(pressedDuration);
			  }
		  }
		}
	}

	private String encodeDuration(long pressedDuration) {
		if(pressedDuration<500){
			return ".";
		}
		return "-";
	}
	
}
