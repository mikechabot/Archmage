package m3rlin;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import m3rlin.g3t.GraciousGet;

public class Executioner implements Runnable {

	private Thread thread;
	private BlockingQueue<Runnable> queue;
	private ThreadPoolExecutor executor;
	private String host;
	private int port;	
	private int threadCount = 400;
	private int interval = 2000;
	boolean isRunning;
	
	public Executioner(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() {
		isRunning = true;
		queue = new ArrayBlockingQueue<Runnable>(100);
		executor = new ThreadPoolExecutor(threadCount, threadCount + 50, interval + 500, TimeUnit.MILLISECONDS, queue, new ThreadPoolExecutor.DiscardPolicy());
		if (thread == null) { 
			thread = new Thread(this);
			thread.start();
		}
    }
	
	public void stop() {		
		isRunning = false;
		executor.shutdown();
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int getActiveCount() {
		return executor.getActiveCount();
	}
		
	@Override
	public void run() {
		do {
			executor.submit(new GraciousGet(host, port, interval));
		} while (isRunning);
	}
}