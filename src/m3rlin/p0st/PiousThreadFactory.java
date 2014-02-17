package m3rlin.p0st;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import m3rlin.g3t.GraciousThreadFactory;

import org.apache.log4j.Logger;

public class PiousThreadFactory implements ThreadFactory {

	private static Logger log = Logger.getLogger(GraciousThreadFactory.class);
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	
	ThreadGroup group;
	String prefix;
	AtomicInteger threadNumber = new AtomicInteger(1);
	
	public PiousThreadFactory() {
		log.info("Initializing PiousThreadFactory...");
		 SecurityManager securityManager = System.getSecurityManager();
         group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
         prefix = "pious-" + poolNumber.getAndIncrement() + "-thread-";
	}
		
	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(group, runnable, prefix + threadNumber.getAndIncrement(), 0);
		return thread;
	}
}
