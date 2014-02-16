package m3rlin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
	
	public static void info(String logEntry) {
		System.out.println(df.format(new Date()) + " |  INFO | " + logEntry);
	}
	
	public static void error(String logEntry) {
		System.out.println(df.format(new Date()) + " | ERROR | " + logEntry);
	}
	
	public static void console(String logEntry) {
		System.out.print(logEntry);
	}
}
