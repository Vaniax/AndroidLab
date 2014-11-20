package service.test;

import javax.persistence.EntityManager;

import entities.Appointment;
import entities.LoginData;
import entities.User;
import service.InstaMeetService;
import sun.font.CreatedFontTracker;


public class TestJpaStuff {
	public static void main(String[] args) {
		
		
		LoginData loginData;
		
		// TODO Auto-generated method stub
		InstaMeetService instaService = new InstaMeetService();
		


		
		loginData = instaService.login("admin", "password");
		System.out.println("Security Token: " + loginData.getToken());
		
		User myAcc = instaService.getOwnData(loginData.getToken(), loginData.getUserId());
		
		EntityManager em = instaService.getEm();
		TestJpaStuff test = new TestJpaStuff();
		test.createUsers(em);
		
		Appointment appoint = new Appointment();
		appoint.setHoster(myAcc);
		appoint.setTitle("Pulvern beim Arbeitsamt");
		appoint.setDescription("Hartz4 abholen");
		appoint.setLattitude(5.4f);
		appoint.setLongitude(3.3f);		
		appoint.getVisitingUsers().add(myAcc);
		myAcc.getVisitingAppointments().add(appoint);
		instaService.CreateAppointments(loginData.getToken(), loginData.getUserId(), appoint);
		System.out.println("Number of hosted Appointments: " + myAcc.getHostedAppointments().size());
		
	}
	
	public void createUsers(EntityManager em) {
		em.getTransaction().begin();
		User admin = new User();
		admin.setUsername("admini");
		admin.setPassword("pw");
		admin.setSalt("xpassword");
		admin.setLattitude(0.f);
		admin.setLongitude(0.f);
		
		User newUser = new User();
		newUser.setUsername("Mr.Y");
		newUser.setPassword("xpassword");
		newUser.setSalt("xpassword");
		newUser.setLattitude(0.f);
		newUser.setLongitude(0.f);
		
		User newUser2 = new User();
		newUser2.setUsername("Mr.Z");
		newUser2.setPassword("xpassword");
		newUser2.setSalt("xpassword");
		newUser2.setLattitude(0.f);
		newUser2.setLongitude(0.f);


		
		newUser.getFriends().add(newUser2);
		newUser2.getFriends().add(newUser);
		
		em.persist(newUser);
		em.persist(newUser2);
		
		em.getTransaction().commit();
		
	}

}
