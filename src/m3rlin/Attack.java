package m3rlin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import m3rlin.utils.Logger;

public abstract class Attack implements Runnable {
	
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
		Logger.info(Thread.currentThread().getName() + " - stopping");
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
