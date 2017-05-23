package cz.fi.muni.ia158.messenger;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class TouchSensorControl implements Runnable{

	public static volatile boolean shouldEnded = false;
	private BlockingQueue<String> messages;

	public TouchSensorControl(BlockingQueue<String> messages) {
		this.messages = messages;
	}
	
	@Override
	public void run(){
		System.out.println("input message");
		Port port = LocalEV3.get().getPort("S2");
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
		  if(shouldEnded){
			  return;
		  }
		  if(encodeLetterCondition(currentLetter, pressedDuration)){
				String encodedLetter = MorseDecoder.decode(currentLetter);
				//System.out.println("char: " + encodedLetter);
				System.out.println("decoded.");
				try {
					messages.put(encodedLetter);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  currentLetter = "";
		  }
		  if( buttonChanged(pastValue, currentValue) ){
			  pastValue = currentValue;
			  lastTime = System.currentTimeMillis();
			  if(buttonReleased(currentValue)){
				  currentLetter += encodeDuration(pressedDuration);
				  //System.out.println("pd: " + pressedDuration + " " + currentLetter);
			  }
		  }
		}
	}

	private boolean buttonReleased(boolean currentValue) {
		return currentValue == false;
	}

	private boolean buttonChanged(boolean pastValue, boolean currentValue) {
		return currentValue != pastValue;
	}

	private boolean encodeLetterCondition(String currentLetter, long pressedDuration) {
		return pressedDuration>2000 && !currentLetter.isEmpty();
	}

	private boolean endCondition(long pressedDuration) {
		return pressedDuration>10000;
	}

	private String encodeDuration(long pressedDuration) {
		if(pressedDuration<300){
			return ".";
		}
		return "-";
	}
	
}
