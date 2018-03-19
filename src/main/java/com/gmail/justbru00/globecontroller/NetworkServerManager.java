package com.gmail.justbru00.globecontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static com.gmail.justbru00.globecontroller.GlobeControllerServerMain.*;

public class NetworkServerManager {

	private static ServerSocket listener;
	private static int clientNumber = 0;
	private static ArrayList<ClientManager> clients = new ArrayList<ClientManager>();
	
	public static void startServer() {
		
		try {
			Messager.info("Creating ServerSocket...");
			listener = new ServerSocket(2018);
			Messager.info("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		try {
			while (GlobeControllerServerMain.RUNNING) {
			Messager.info("Waiting for any CLIENT");
			// Create client handler
			ClientManager client = new ClientManager(listener.accept(), clientNumber++);
			client.start();
			clients.add(client);
			}
		} catch (SocketException e2){
			Messager.warn("Socket Closed");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void closeServer() {
		
		for (ClientManager c : clients) {
			c.shutdown();
		}
		
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class ClientManager extends Thread {
        private Socket socket;
        private int clientNumber;
        private boolean stop = false;

        public ClientManager(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            Messager.info("New connection with client# " + clientNumber + " at " + socket);
        }
        
        public void shutdown() {
        	stop = true;
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back DONE <cmdname>
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("CONNECTION ACCEPTED");

                // Get messages from the client, line by line; 
                while (!stop) {
                    String input = in.readLine();
                    if (input == null || input.equalsIgnoreCase("DISCONNECT")) {
                        break;
                    }                     
                   
                    if (input.equalsIgnoreCase("BLUEON")) {
                    	BLUE_GLOBES.setState(LEDState.ON);
                    	out.println("DONE BLUEON");
                    	Messager.info("Client #" + clientNumber + " commanded BLUEON");
                    } else if (input.equalsIgnoreCase("BLUEOFF")) {
                    	BLUE_GLOBES.setState(LEDState.OFF);
                    	out.println("DONE BLUEOFF");
                    	Messager.info("Client #" + clientNumber + " commanded BLUEOFF");
                    } else if (input.equalsIgnoreCase("WHITEON")) {
                    	WHITE_GLOBES.setState(LEDState.ON);
                    	out.println("DONE WHITEON");
                    	Messager.info("Client #" + clientNumber + " commanded WHITEON");
                    } else if (input.equalsIgnoreCase("WHITEOFF")) {
                    	WHITE_GLOBES.setState(LEDState.OFF);
                    	out.println("DONE WHITEOFF");
                    	Messager.info("Client #" + clientNumber + " commanded WHITEOFF");
                    } else if (input.equalsIgnoreCase("BOTHON")) {
                    	WHITE_GLOBES.setState(LEDState.ON);
                    	BLUE_GLOBES.setState(LEDState.ON);
                    	out.println("DONE BOTHON");
                    	Messager.info("Client #" + clientNumber + " commanded BOTHON");
                    } else if (input.equalsIgnoreCase("BOTHOFF")) {
                    	WHITE_GLOBES.setState(LEDState.OFF);
                    	BLUE_GLOBES.setState(LEDState.OFF);
                    	out.println("DONE BOTHOFF");
                    	Messager.info("Client #" + clientNumber + " commanded BOTHOFF");
                    } else if (input.equalsIgnoreCase("BLUEONWHITEOFF")) {
                    	WHITE_GLOBES.setState(LEDState.OFF);
                    	BLUE_GLOBES.setState(LEDState.ON);
                    	out.println("DONE BLUEONWHITEOFF");
                    	Messager.info("Client #" + clientNumber + " commanded BLUEONWHITEOFF");
                    } else if (input.equalsIgnoreCase("BLUEOFFWHITEON")) {
                    	WHITE_GLOBES.setState(LEDState.ON);
                    	BLUE_GLOBES.setState(LEDState.OFF);
                    	out.println("DONE BLUEOFFWHITEON");
                    	Messager.info("Client #" + clientNumber + " commanded BLUEOFFWHITEON");
                    }else if (input.equalsIgnoreCase("PING")) {
                    	out.println("PONG");
                    	Messager.info("-> RECEIVED PING -- Sent PONG <-");
                    } else {
                    	out.println("UNKNOWN COMMAND");
                    }
                }
            } catch (IOException e) {
               Messager.warn("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    Messager.critical("Couldn't close a socket, what's going on?");
                }
               Messager.info("Connection with client# " + clientNumber + " closed");
            }
        }
	
}
}
