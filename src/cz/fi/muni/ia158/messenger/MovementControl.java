package cz.fi.muni.ia158.messenger;

import java.util.concurrent.BlockingQueue;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;

public class MovementControl implements Runnable {
	
	private BlockingQueue<String> messages;

	public MovementControl(BlockingQueue<String> messages) {
		this.messages = messages;
	}

	@Override
	public void run(){
	 int DEFAULT_MOTOR_SPEED = 200;

     EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S4);
     
     EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.A);
     EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.D);

     motorRight.setSpeed(DEFAULT_MOTOR_SPEED);
     motorLeft.setSpeed(DEFAULT_MOTOR_SPEED);

     while (true) {
         int commandChan1 = irSensor.getRemoteCommand(0);
         int commandChan2 = irSensor.getRemoteCommand(1);
         int commandChan3 = irSensor.getRemoteCommand(2);

         // CENTRE/BEACON on Chanel 1
         if (commandChan1 == 9) {
        	 destroyMessage();
         }
         // CENTRE/BEACON on Chanel 2
         if (commandChan2 == 9) {
        	 showMessage();
         }
         if(commandChan3 == 9) {
        	 TouchSensorControl.shouldEnded = true;
        	 break;
         }


         // Controller Part
         switch (commandChan1) {
             case 5: motorRight.forward();
                     motorLeft.forward();
                     break;
             case 6: motorRight.backward();
                     motorLeft.forward();
                     break;
             case 7: motorRight.forward();
                     motorLeft.backward();
                     break;
             case 8: motorRight.backward();
                     motorLeft.backward();
                     break;
             default:   
                 if (motorLeft.isMoving() || motorRight.isMoving()) {
                     motorLeft.stop(true);
                     motorRight.stop(true);
                 }
         }

         // Pause
         try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     motorLeft.stop(true);
     motorRight.stop(true);

     irSensor.close();

     motorLeft.close();
     motorRight.close();
 }

	private void destroyMessage() {
		System.out.println("message destroyed, muhaha");
		 while(!messages.isEmpty()){
			 try {
				messages.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 Delay.msDelay(4000);
	}

	private void showMessage() {
		System.out.println("message is:");
		while(!messages.isEmpty()){
			 try {
				System.out.print(messages.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		  Delay.msDelay(4000);
		  LCD.clear();
		
		
	}
}
