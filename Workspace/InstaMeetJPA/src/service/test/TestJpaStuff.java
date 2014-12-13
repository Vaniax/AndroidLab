package service.test;

import java.util.Set;





import service.InstaMeetService;
import simpleEntities.LoginData;
import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;


public class TestJpaStuff {
	public static void main(String[] args) {
		
		
		LoginData loginData;
		
		// TODO Auto-generated method stub
		InstaMeetService instaService = new InstaMeetService();
		
		SimpleUser newUser = instaService.createUser("MrTest3r", "test");
		if(newUser == null) {
			System.err.println("User already existing");
		} else {
			System.out.println("User " + newUser.getUsername() + " created! ID: " + newUser.getId());
		}
		

		
		loginData = instaService.login("MrTest3r", "test");
		if(loginData != null) {
			System.out.println("Security Token: " + loginData.getToken());
		} else {
			System.err.println("Login failed");
		}
		SimpleUser myAcc = instaService.getOwnData(loginData.getToken(), loginData.getUserId());
		
		
		Set<Integer> friendIds = myAcc.getFriends();
		for(Integer id : friendIds) {
			System.out.println("Friend id: " + id);
		}

		for(Integer a :myAcc.getHostedAppointments()) {
			System.out.println("Appointment #id: " + a);
		}
		
		instaService.addFriend(loginData.getToken(), myAcc.getId(), 3);
		instaService.addFriend(loginData.getToken(), myAcc.getId(), 2);
		instaService.addFriend(loginData.getToken(), myAcc.getId(), 1);
		//instaService.addFriendRequest(loginData.getToken(), myAcc.getId(), 3);
		instaService.addFriendReply(loginData.getToken(), myAcc.getId(), 3, true);
		
		instaService.visitAppointment(loginData.getToken(), myAcc.getId(), 3);

		
		
		SimpleAppointment appoint = new SimpleAppointment();
		appoint.setTitle("Pulvern beim Arbeitsamt");
		appoint.setDescription("Hartz4 abholen");
		appoint.setLattitude(5.4f);
		appoint.setLongitude(3.3f);		
		instaService.createAppointment(loginData.getToken(), loginData.getUserId(), appoint);
		System.out.println("Number of hosted Appointments: " + myAcc.getHostedAppointments().size());
		
	}

}
