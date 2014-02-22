package m3rlin;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import m3rlin.g3t.GraciousGet;
import m3rlin.g3t.GraciousThreadFactory;
import m3rlin.p0st.PiousPost;
import m3rlin.p0st.PiousThreadFactory;

public class Executioner implements Runnable {

	private static Logger log = Logger.getLogger(Executioner.class);
	
	private Thread thread;
	private BlockingQueue<Runnable> piousQueue, graciousQueue;
	private ThreadPoolExecutor pious, gracious;
	
	private String host;
	private int port;	
	private int connections = 200;
	private int interval = 2000;
	boolean isRunning;
	
	public Executioner(String host, int port) {
		this.host = host;
		this.port = port;
	}	
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int getGraciousActiveCount() {		
		return gracious.getActiveCount();
	}
	
	public int getPiousActiveCount() {
		return pious.getActiveCount();
	}
	
	public long getGraciousCompletedCount() {
		return gracious.getCompletedTaskCount();
	}
	
	public long getPiousCompletedCount() {
		return pious.getCompletedTaskCount();
	}
	
	public long getGraciousTaskCount() {
		return gracious.getTaskCount();
	}
	
	public long getPiousTaskCount() {
		 return pious.getTaskCount();
	}	

	public void start() {
		isRunning = true;
		piousQueue = new ArrayBlockingQueue<Runnable>(connections);
		graciousQueue = new ArrayBlockingQueue<Runnable>(connections);
		pious = new ThreadPoolExecutor(connections, connections, interval + 500, TimeUnit.MILLISECONDS, piousQueue, new PiousThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
		gracious = new ThreadPoolExecutor(connections, connections, interval + 500, TimeUnit.MILLISECONDS, graciousQueue,  new GraciousThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
		if (thread == null) { 
			thread = new Thread(this);
			thread.start();
		}
    }
	
	public void stop() {		
		isRunning = false;
		log.info("Commencing executioner shutdown...");
		pious.shutdownNow();
		gracious.shutdownNow();
		try {
			log.info("Awaiting further thread termination...");
			pious.awaitTermination(30*1000, TimeUnit.MILLISECONDS);
			gracious.awaitTermination(30*1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) { }
		log.info("Executioner shutdown complete...");
	}
	
	@Override
	public void run() {
		while(isRunning) {
			if (piousQueue.remainingCapacity() > 0) {
				Future<?> future = pious.submit(new PiousPost(host, port, interval));
				log.debug("Submitting pious future (" + future.toString() +"), piousQueue=" + piousQueue.size());
			}
			if (graciousQueue.remainingCapacity() > 0) {
				Future<?> future = gracious.submit(new GraciousGet(host, port, interval));				
				log.debug("Submitting gracious future (" + future.toString() +"), graciousQueue=" + graciousQueue.size());	
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Do nothing
			}
		}
	}
}