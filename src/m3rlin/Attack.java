package m3rlin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public abstract class Attack implements Runnable {
	
	private static Logger log = Logger.getLogger(Attack.class);

	protected Socket socket;
	protected BufferedWriter writer;
	protected String hostname;
	protected int port;
	protected int interval;
	protected boolean isRunning;

	public Attack(String hostname, int port, int interval) {
		this.hostname = hostname;
		this.port = port;
		this.interval = interval;
	}
	
	public void attack() throws IOException {
		// TANGO DOWN
	}

	public void writeHeaders() throws IOException { }

	public void stop() {
		log.info("Stopping Attack thread...");
		isRunning = false;
		try {
			socket.close();
		} catch (IOException e) { 
			// Do nothing
		}
	}

	public boolean isInterrupted() {
		return Thread.currentThread().isInterrupted();
	}

	@Override
	public void run() {
		try {
			isRunning = true;
			socket = new Socket(InetAddress.getByName(hostname), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			log.info("Attack thread started...");
			writeHeaders();
			while(isRunning && !isInterrupted()) {
				attack();
				try {						
					log.debug("Sleeping thread...");
					Thread.sleep(interval);						
				} catch (InterruptedException e) { 
					log.debug("Interupped!");
					stop();
				}
			}
		} catch (UnknownHostException e) {
			log.error("Could not locate the host (UnknownHostException)");				
		} catch (ConnectException e) {
			log.error("Could not connect to the socket address, check the port (ConnectException)");			
		} catch (SocketException e) {
			log.error("Error creating or accessing socket (SocketException)");				
		} catch (IOException e1) {
			log.error("I/O error with socket (IOException)");
		} finally {
			stop();
		}
	}
}