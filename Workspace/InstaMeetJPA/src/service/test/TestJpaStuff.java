package service.test;

import java.util.Set;




import service.InstaMeetService;
import simpleEntities.LoginData;
import simpleEntities.simpleUser;


public class TestJpaStuff {
	public static void main(String[] args) {
		
		
		LoginData loginData;
		
		// TODO Auto-generated method stub
		InstaMeetService instaService = new InstaMeetService();
		
		simpleUser newUser = instaService.createUser("MrTest3r", "test");
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
		simpleUser myAcc = instaService.getOwnData(loginData.getToken(), loginData.getUserId());
		
		
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

		
		instaService.visitAppointment(loginData.getToken(), myAcc.getId(), 3);

		
		
//		Appointment appoint = new Appointment();
//		appoint.setHoster(myAcc);
//		appoint.setTitle("Pulvern beim Arbeitsamt");
//		appoint.setDescription("Hartz4 abholen");
//		appoint.setLattitude(5.4f);
//		appoint.setLongitude(3.3f);		
//		appoint.getVisitingUsers().add(myAcc);
//		myAcc.getVisitingAppointments().add(appoint);
//		instaService.CreateAppointments(loginData.getToken(), loginData.getUserId(), appoint);
//		System.out.println("Number of hosted Appointments: " + myAcc.getHostedAppointments().size());
		
	}

}
