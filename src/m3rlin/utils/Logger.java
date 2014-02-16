package m3rlin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	private SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
	
	private String clazz;
	
	public Logger(String clazz) {
		this.clazz = clazz;
	}
	
	public void info(String logEntry) {
		System.out.println(df.format(new Date()) + " |  INFO | " + clazz + " - " + logEntry);
	}
	
	public void error(String logEntry) {
		System.out.println(df.format(new Date()) + " | ERROR | " + clazz + " - " + logEntry);
	}
	
	public  void console(String logEntry) {
		System.out.print(logEntry);
	}
}
