package service;

import javax.xml.ws.Endpoint;

public class InstaMeetServer {
	public static final String WSDL = "http://localhost:1234/InstaMeet?wsdl";

	public static void main(String[] args) {
		ServiceInterface instameetService = new InstaMeetService();
		Endpoint e = Endpoint.publish(WSDL, instameetService);
		System.out.println("InstaMeetService ready: " + e.isPublished());
	
	
		while(true) {
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
}
