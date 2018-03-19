package com.gmail.justbru00.globecontroller;

import java.util.Scanner;

import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.Console;

public class GlobeControllerServerMain {
	
	public static final int LED_TIMER_INTERVAL = 200;
	public static LED BLUE_GLOBES = new LED(RaspiPin.GPIO_00, "Blue Globes", true);
	public static LED WHITE_GLOBES = new LED(RaspiPin.GPIO_02, "White/Clear Globes", true);
	public static LED STATUS = new LED(RaspiPin.GPIO_04, "Status LED", false);
	public static boolean DEBUG = true;
	public static final Console CONSOLE = new Console();
	public static boolean RUNNING = true;

	public static void main(String[] args) {
		
		CONSOLE.title("<-- GlobeControllerServer -->");
		CONSOLE.box("GlobeControllerServer is Copyright 2018 Justin Brubaker.",
				"GlobeControllerServer uses the PI4J Project. The PI4J project is licensed under the LGPLv3. A copy of this license can be found at http://pi4j.com/license.html.");
		CONSOLE.box("Type 'stop' to stop the program");
		
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("-selftest")) {
				globeTest();
			}
		}
		
		new Thread(() -> {
			Scanner input = new Scanner(System.in);
			while (RUNNING) {
				/* if (input.hasNext()) { */
				String txt = input.next();

				if (txt.equalsIgnoreCase("stop")) {
					RUNNING = false;
					Messager.info("Received stop request. Program should stop in ~1 second.");
				} else if (txt.equalsIgnoreCase("BLUEON")) {
					BLUE_GLOBES.setState(LEDState.ON);
					Messager.info("DONE BLUEON");
				} else if (txt.equalsIgnoreCase("BLUEOFF")) {
					BLUE_GLOBES.setState(LEDState.OFF);
					Messager.info("DONE BLUEOFF");
				} else if (txt.equalsIgnoreCase("WHITEON")) {
					WHITE_GLOBES.setState(LEDState.ON);
					Messager.info("DONE WHITEON");
				} else if (txt.equalsIgnoreCase("WHITEOFF")) {
					WHITE_GLOBES.setState(LEDState.OFF);
					Messager.info("DONE WHITEOFF");
				} else if (txt.equalsIgnoreCase("TEST")) {
					globeTest();
					Messager.info("DONE TEST");
				} else if (txt.equalsIgnoreCase("BOTHON")) {
					BLUE_GLOBES.setState(LEDState.ON);
					WHITE_GLOBES.setState(LEDState.ON);
					Messager.info("DONE BOTHON");
				} else if (txt.equalsIgnoreCase("BOTHOFF")) {
					BLUE_GLOBES.setState(LEDState.OFF);
					WHITE_GLOBES.setState(LEDState.OFF);
					Messager.info("DONE BOTHOFF");
				} else if (txt.equalsIgnoreCase("BLUEONWHITEOFF")) {
					BLUE_GLOBES.setState(LEDState.ON);
					WHITE_GLOBES.setState(LEDState.OFF);
					Messager.info("DONE BLUEONWHITEOFF");
				} else if (txt.equalsIgnoreCase("BLUEOFFWHITEON")) {
					BLUE_GLOBES.setState(LEDState.OFF);
					WHITE_GLOBES.setState(LEDState.ON);
					Messager.info("DONE BLUEOFFWHITEON");
				} else if (txt.equalsIgnoreCase("BLACKOUT")) {
					BLUE_GLOBES.setState(LEDState.OFF);
					WHITE_GLOBES.setState(LEDState.OFF);
					Messager.info("DONE BLACKOUT");
				}

				input.nextLine();
				/* } */
			}
			input.close();
		}).start();
		
		// Set the status LED to flashing
		STATUS.setState(LEDState.FLASHING_FAST);
		Messager.info("Status LED has been set to FLASHING_FAST");
		
		// Shut everything off
		BLUE_GLOBES.setState(LEDState.OFF);
		WHITE_GLOBES.setState(LEDState.OFF);
		Messager.info("BLUE_GLOBES and WHITE_GLOBES should now be OFF");
		
		// Start listening for clients
		Messager.info("Starting NetworkServerManager");
		// Prevent thread blocking
		new Thread(() -> {
			NetworkServerManager.startServer();
		}).start();
		
		
		
		while (RUNNING) {
			
			// WAIT FOR SHUTDOWN
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		BLUE_GLOBES.setState(LEDState.OFF);
		WHITE_GLOBES.setState(LEDState.OFF);
		STATUS.setState(LEDState.OFF);
		
		BLUE_GLOBES.stopTimer();
		WHITE_GLOBES.stopTimer();
		STATUS.stopTimer();
		NetworkServerManager.closeServer();
		// PROGRAM DONE
	}
	
	public static void globeTest() {
		Messager.info("Starting GLOBE TEST");
		// TODO Toggle both lights on and off 3 times waiting a second in between each
		try {
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		// TODO Toggle BLUE than WHITE three times waiting a second in between each
		try {
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.OFF);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.OFF);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
			BLUE_GLOBES.setState(LEDState.ON);
			WHITE_GLOBES.setState(LEDState.ON);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Messager.info("FINISHED GLOBE TEST");
	}
	
}
