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
	
	protected String name = this.getClass().getSimpleName();
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
	
	public boolean isInterrupted() {		
		return Thread.currentThread().isInterrupted();
	}
		
	public void stop() {
		log.info("Stopping dead attack thread...");
		isRunning = false;
		try {
			socket.close();
		} catch (IOException e) { }
	}

	@Override
	public void run() {
		//
	}
}
