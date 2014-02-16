package m3rlin;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import m3rlin.g3t.GraciousGet;
import m3rlin.p0st.PiousPost;
import m3rlin.utils.Logger;

public class Executioner implements Runnable {

	private static Logger log = new Logger("Executioner");
	
	private Thread thread;
	private BlockingQueue<Runnable> piousQueue;
	private BlockingQueue<Runnable> graciousQueue;
	private ThreadPoolExecutor pious;
	private ThreadPoolExecutor gracious;
	
	private String host;
	private int port;	
	private int connections = 1;
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
		pious = new ThreadPoolExecutor(connections, connections, interval + 500, TimeUnit.MILLISECONDS, piousQueue, new ThreadPoolExecutor.DiscardPolicy());
		gracious = new ThreadPoolExecutor(connections, connections, interval + 500, TimeUnit.MILLISECONDS, graciousQueue, new ThreadPoolExecutor.DiscardPolicy());
		if (thread == null) { 
			thread = new Thread(this);
			thread.start();
		}
    }
	
	public void stop() {		
		isRunning = false;
		pious.shutdownNow();
		gracious.shutdownNow();
		log.console("\n>> Commencing executioners shutdown...");
		try {
			log.console("\n>> Waiting for threads to terminate...\n\n");
			pious.awaitTermination(5*60*1000, TimeUnit.MILLISECONDS);			
			gracious.awaitTermination(5*60*1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) { }
		log.console("\n>> Executioners shutdown complete...");
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
	
	public long getTaskCount() {
		return gracious.getTaskCount() + pious.getTaskCount();
	}
	
	@Override
	public void run() {
		while(isRunning) {
			if (pious.getQueue().size() < connections) {
				pious.submit(new PiousPost(host, port, interval));
			}
			if (gracious.getQueue().size() < connections) {
				gracious.submit(new GraciousGet(host, port, interval));
			}
		}
	}
}