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

import entities.Appointment;
import entities.Location;
import entities.LoginData;
import entities.User;

@WebService(name="InstaMeetService", serviceName="mInstaMeetService")
public class InstaMeetService implements ServiceInterface {
	
	private EntityManagerFactory factory;
	
	private Map<String, User> sessions = new HashMap<String, User>();

	
	public EntityManagerFactory getFactory() {
		return factory;
	}

	private EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}


	
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
	public List<User> GetFriends(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@WebMethod
	public List<Appointment> GetAppointments(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
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
	public Appointment CreateAppointments(String SecurityToken, int userId,
			Appointment appointment) {
		// TODO Auto-generated method stub
		if(verifyUser(SecurityToken, userId)) {
			appointment.setHoster(sessions.get(SecurityToken));
			em.getTransaction().begin();
			em.persist(appointment);
			em.getTransaction().commit();
			return appointment;
		}
			
		return null;
	}



	@Override
	public User getOwnData(String SecurityToken, int userId) {
		if(sessions.containsKey(SecurityToken)) {
			return sessions.get(SecurityToken);
		}
		return null;
	}

}
