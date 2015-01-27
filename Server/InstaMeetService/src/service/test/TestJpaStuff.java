package service.test;

import service.InstaMeetService;
import simpleEntities.LoginData;
import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;


public class TestJpaStuff {
	
	InstaMeetService instaService;
	

	public TestJpaStuff() {
		instaService = new InstaMeetService();
	}
	
	public InstaMeetService getInstaService() {
		return instaService;
	}
	
	void createUser(String [] userData) {
		//User creation
		for(int i=0;i<userData.length;i+=2) {
			SimpleUser createUser = instaService.createUser(userData[i], userData[i+1]);
			if(createUser == null) {
				System.err.println("User \"" + userData[i] + "\" already existing");
			} else {
				System.out.println("User " + createUser.getUsername() + " created! ID: " + createUser.getId());
			}			
		}
	}
	
	SimpleAppointment createAppointment(String title, String description, float lat, float lon) {
		SimpleAppointment appoint = new SimpleAppointment();
		appoint.setTitle(title);
		appoint.setDescription(description);
		appoint.setLattitude(lat);
		appoint.setLongitude(lon);	
		return appoint;
	}
	
	
	public static void main(String[] args) {
		String [] userData = { "Hans Peter", "123", "Angelo Merte", "123", "Klaus Günther", "123",  "Hans Wurst", "123" };
//		String [] userData = { "Guido", "testpasswort", "Olaf" , "testpasswort"};
		
		LoginData loginData;
		
		TestJpaStuff jpaTest = new TestJpaStuff();
		
		jpaTest.createUser(userData);
		
		
		loginData = jpaTest.getInstaService().login("Hans Peter", "123");
		if(loginData != null) {
			System.out.println("Security Token: " + loginData.getToken());
		} else {
			System.err.println("Login failed");
		}

		SimpleUser myAcc = jpaTest.getInstaService().getOwnData(loginData.getToken(), loginData.getUserId());
		jpaTest.getInstaService().addFriendRequest(loginData.getToken(), myAcc.getId(), 13);
		jpaTest.getInstaService().addFriendRequest(loginData.getToken(), myAcc.getId(), 14);
		jpaTest.getInstaService().addFriendRequest(loginData.getToken(), myAcc.getId(), 15);
		
		
		loginData = jpaTest.getInstaService().login("Angelo Merte", "123");
		myAcc = jpaTest.getInstaService().getOwnData(loginData.getToken(), loginData.getUserId());
		jpaTest.getInstaService().addFriendReply(loginData.getToken(), myAcc.getId(), 12, true);

		loginData = jpaTest.getInstaService().login("Klaus Günther", "123");
		myAcc = jpaTest.getInstaService().getOwnData(loginData.getToken(), loginData.getUserId());
		jpaTest.getInstaService().addFriendReply(loginData.getToken(), myAcc.getId(), 12, true);

		loginData = jpaTest.getInstaService().login("Hans Wurst", "123");
		myAcc = jpaTest.getInstaService().getOwnData(loginData.getToken(), loginData.getUserId());
		jpaTest.getInstaService().addFriendReply(loginData.getToken(), myAcc.getId(), 12, true);
		
		
//		SimpleAppointment appoint = jpaTest.createAppointment("TU-BS Sit in", "Pizza und Bier", 52.27303f, 10.52514f);
//		jpaTest.getInstaService().createAppointment(loginData.getToken(), loginData.getUserId(), appoint);
//		System.out.println("Number of hosted Appointments: " + myAcc.getHostedAppointments().size());
//		
	}

}
