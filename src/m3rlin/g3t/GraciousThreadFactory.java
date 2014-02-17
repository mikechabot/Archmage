package m3rlin.g3t;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class GraciousThreadFactory implements ThreadFactory {

	private static Logger log = Logger.getLogger(GraciousThreadFactory.class);
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	
	AtomicInteger threadNumber = new AtomicInteger(1);
	ThreadGroup group;
	String prefix;
	
	public GraciousThreadFactory() {
		log.info("Initializing GraciousThreadFactory...");
		SecurityManager securityManager = System.getSecurityManager();
		group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
		prefix = "gracious-" + poolNumber.getAndIncrement() + "-thread-";
	}
		
	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(group, runnable, prefix + threadNumber.getAndIncrement(), 0);
		return thread;
	}
}
