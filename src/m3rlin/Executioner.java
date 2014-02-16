package m3rlin;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import m3rlin.g3t.GraciousGet;
import m3rlin.p0st.PiousPost;
import m3rlin.utils.Logger;

public class Executioner implements Runnable {

	private Thread thread;
	private BlockingQueue<Runnable> piousQueue;
	private BlockingQueue<Runnable> graciousQueue;
	private ThreadPoolExecutor pious;
	private ThreadPoolExecutor gracious;
	private String host;
	private int port;	
	private int connections = 400;
	private int interval = 2000;
	boolean isRunning;
	
	public Executioner(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() {
		isRunning = true;
		piousQueue = new ArrayBlockingQueue<Runnable>(connections);
		graciousQueue = new ArrayBlockingQueue<Runnable>(connections);
		pious = new ThreadPoolExecutor(connections, connections + 50, interval + 500, TimeUnit.MILLISECONDS, piousQueue, new ThreadPoolExecutor.DiscardPolicy());
		gracious = new ThreadPoolExecutor(connections, connections + 50, interval + 500, TimeUnit.MILLISECONDS, graciousQueue, new ThreadPoolExecutor.DiscardPolicy());
		if (thread == null) { 
			thread = new Thread(this);
			thread.start();
		}
    }
	
	public void stop() {		
		isRunning = false;
		pious.shutdown();
		gracious.shutdown();
		Logger.console("\n>> Commencing executioners shutdown...\n");
		try {
			Logger.console(">> Waiting for threads to terminate...\n\n");
			pious.awaitTermination(5*60*1000, TimeUnit.MILLISECONDS);
			gracious.awaitTermination(5*60*1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			pious.shutdownNow();
			gracious.shutdownNow();
		}
		Logger.console("\n>> Executioners shutdown complete...");
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int getActiveCount() {
		return gracious.getActiveCount() + pious.getActiveCount();
	}
	
	public long getCompletedTaskCount() {
		return gracious.getCompletedTaskCount() + pious.getCompletedTaskCount();
	}
	
	@Override
	public void run() {
		while(isRunning) {
			gracious.submit(new GraciousGet(host, port, interval));
			pious.submit(new PiousPost(host, port, interval));
		}
	}
}