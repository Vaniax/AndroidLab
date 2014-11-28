package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import simpleEntities.Location;
import simpleEntities.LoginData;
import simpleEntities.simpleAppointment;
import simpleEntities.simpleUser;
import entities.Appointment;
import entities.User;

@WebService(name="InstaMeetService", serviceName="mInstaMeetService")
public class InstaMeetService implements ServiceInterface {
	
	private EntityManagerFactory factory;
	
	private Map<String, User> sessions = new HashMap<String, User>();

	


	private EntityManager em;
	
	

	
	public InstaMeetService() {
		// TODO Auto-generated constructor stub
		factory = Persistence.createEntityManagerFactory("InstaMeetJPA");
		em = factory.createEntityManager();
		
	}

	@WebMethod
	public LoginData login(String name, String passwort) {
		TypedQuery<User> results = em.createNamedQuery("User.login", User.class)
			    .setParameter("name", name)
			    .setParameter("password", passwort);
		try {
			User correctUser = results.getSingleResult();	
			String token = name + passwort;
			sessions.put(token, correctUser);

			return new LoginData(token, correctUser.getId());
		} catch(NonUniqueResultException e) {
			System.out.println("non unique login!");
			return null;
		} catch(NoResultException e) {
			System.out.println("Login Data not found!");
			return null;
		}
		

		
	}

	@WebMethod
	public Map<Integer, simpleUser> GetFriends(String SecurityToken, int userId) {

		if(verifyUser(SecurityToken, userId)) {
			Map<Integer, simpleUser> friendList = new HashMap<Integer, simpleUser>();
			
			for(User u : sessions.get(SecurityToken).getFriends().values()) {
				friendList.put(u.getId(), new simpleUser(u));
			}
			return friendList;
		}
		return null;
	}

	@WebMethod
	public Map<Double, simpleAppointment> GetNearAppointments(String SecurityToken, int userId, Location location) {
		if(verifyUser(SecurityToken, userId)) {
			//TODO Filter near locations
			Map<Double, simpleAppointment> appList = new HashMap<Double, simpleAppointment>();
			
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = em.createNamedQuery("Appointment.nearby")
					.setParameter("lat", location.getLattitude())
					.setParameter("lon", location.getLongitude())
					.getResultList();
			
			for(Object[] result : resultList) {
				Appointment app = ((Appointment)result[0]);
				simpleAppointment sApp = new simpleAppointment(app);
				sApp.setDistance((double)result[1]);
				
				appList.put((double)result[1], sApp);
				
			}			
			

			return appList;
		}
		return null;
	}

	@WebMethod
	public Map<Integer, simpleAppointment> GetMyVisitingAppointments(String SecurityToken, int userId) {
		if(verifyUser(SecurityToken, userId)) {
			Map<Integer, simpleAppointment> appList = new HashMap<Integer, simpleAppointment>();
			
			for(Appointment a : sessions.get(SecurityToken).getVisitingAppointments().values()) {
				appList.put(a.getId(), new simpleAppointment(a));
			}
			return appList;
		}
		return null;
	}

	@WebMethod
	public Location GetFriendLocation(String SecurityToken, int userId,
			int friendId) {
		// TODO Auto-generated method stub
		return null;
	}

	@WebMethod
	public List<Location> GetFriendLocations(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@WebMethod
	public List<String> GetMessages(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@WebMethod
	public boolean SendMessage(String SecurityToken, int userId, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@WebMethod
	public boolean UpdateLocation(String SecurityToken, int userId,
			Location location) {
		if(verifyUser(SecurityToken, userId)) {
			User ownUser = sessions.get(SecurityToken);
			ownUser.setLattitude(location.getLattitude());
			ownUser.setLattitude(location.getLattitude());
			
		}
		return false;
	}


	public boolean verifyUser(String SecurityToken, int userId) {
		if(sessions.containsKey(SecurityToken)) {
			if(sessions.get(SecurityToken).getId() == userId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public simpleAppointment CreateAppointments(String SecurityToken, int userId,
			simpleAppointment sAppointment) {
		// TODO Auto-generated method stub
		if(verifyUser(SecurityToken, userId)) {
			Appointment appointment = new Appointment(sAppointment);
			appointment.setHoster(sessions.get(SecurityToken));
			em.getTransaction().begin();
			em.persist(appointment);
			em.getTransaction().commit();
			return new simpleAppointment(appointment);
		}
			
		return null;
	}



	@Override
	public simpleUser getOwnData(String SecurityToken, int userId) {
		if(sessions.containsKey(SecurityToken)) {
			return new simpleUser(sessions.get(SecurityToken));
		}
		return null;
	}



	@Override
	public simpleUser createUser(String name, String password) {
		TypedQuery<User> results = em.createNamedQuery("User.findName", User.class).setParameter("name", name);
		try {
			results.getSingleResult();
			return null;

		} catch(NoResultException e) {
			User newUser = new User();
			newUser.setUsername(name);
			//TODO: generate salt stuff
			newUser.setPassword(password);
			em.getTransaction().begin();
			em.persist(newUser);
			em.getTransaction().commit();	
			return new simpleUser(newUser);			
		}
		

	}



	@Override
	public boolean visitAppointment(String SecurityToken, int userId,
			int appointmentId) {
		if(verifyUser(SecurityToken, userId)) {
			try {
				User usr = sessions.get(SecurityToken);
				TypedQuery<Appointment> result = em.createNamedQuery("Appointment.findId", Appointment.class)
						.setParameter("id", appointmentId);
				Appointment app = result.getSingleResult();

				em.getTransaction().begin();
				if(!app.getVisitingUsers().containsKey(usr.getId())) {
					app.getVisitingUsers().put(usr.getId(), usr);
				}
				if(!usr.getVisitingAppointments().containsKey(app.getId())) {
					usr.getVisitingAppointments().put(app.getId(),app);					
				}
				em.getTransaction().commit();
				return true;
			} catch(NoResultException e) {
				System.out.println("Appointment not found.");
			}
		}
		return false;
	}



	@Override
	public boolean addFriend(String SecurityToken, int userId, int friendId) {
		// TODO Auto-generated method stub
		if(verifyUser(SecurityToken, userId)) {
			try {
				User usr = sessions.get(SecurityToken);
				TypedQuery<User> result = em.createNamedQuery("User.findId", User.class)
						.setParameter("id", friendId);
				User friend = result.getSingleResult();

				em.getTransaction().begin();
				if(!friend.getFriends().containsKey(usr.getId())) {
					friend.getFriends().put(usr.getId(), usr);
				}
				if(!usr.getFriends().containsKey(friend.getId())) {
					usr.getFriends().put(friend.getId(), friend);					
				}
				em.getTransaction().commit();
				return true;
			} catch(NoResultException e) {
				System.out.println("Appointment not found.");
			}
		}
		return false;
	}

}
