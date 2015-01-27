package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import simpleEntities.Location;
import simpleEntities.LoginData;
import simpleEntities.SensitiveData;
import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import entities.Appointment;
import entities.ConfirmedFriend;
import entities.UnconfirmedFriend;
import entities.User;

public class InstaMeetService implements ServiceInterface {
	
	private EntityManagerFactory factory;
	
	private Map<String, User> sessions = new HashMap<String, User>();

	


	private EntityManager em;
	
	

	
	public InstaMeetService() {
		factory = Persistence.createEntityManagerFactory("InstaMeetJPA");
		em = factory.createEntityManager();
	}

	public LoginData login(String name, String passwort) {
		TypedQuery<User> results = em.createNamedQuery("User.findName", User.class)
			    .setParameter("name", name);
		try {
			User correctUser = results.getSingleResult();	
			CryptoService cryptoService = new CryptoService();
			if(cryptoService.verifyLogin(correctUser.getPassword(), correctUser.getSalt(), passwort))  {
				String token = cryptoService.generateToken(correctUser.getId());
				sessions.put(token, correctUser);
				return new LoginData(token, correctUser.getId());				
			} else {
				System.out.println("Wrong password!");
				return null;
			}
			
			
		} catch(NonUniqueResultException e) {
			System.out.println("non unique login!");
			return null;
		} catch(NoResultException e) {
			System.out.println("Login Data not found!");
			return null;
		}
	}

	public List<SimpleUser> GetFriends(String SecurityToken, int userId) {

		if(verifyUser(SecurityToken, userId)) {
			List<SimpleUser> friendList = new ArrayList<SimpleUser>();
			
			for(User u : sessions.get(SecurityToken).getFriends()) {
				friendList.add(u.toSimpleUser());
			}
			return friendList;
		}
		return null;
	}

	public List<SimpleAppointment> GetNearAppointments(String SecurityToken, int userId, Location location) {
		if(verifyUser(SecurityToken, userId)) {
			//TODO Filter near locations
			List<SimpleAppointment> appList = new ArrayList<SimpleAppointment>();
			
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = em.createNamedQuery("Appointment.nearby")
					.setParameter("lat", location.getLattitude())
					.setParameter("lon", location.getLongitude())
					.getResultList();
			
			int i=0;
			for(Object[] result : resultList) {
				Appointment app = ((Appointment)result[0]);
				SimpleAppointment sApp = app.toSimpleAppointment();
				sApp.setDistance((double)result[1]);
				
				appList.add(i++,sApp);
				
			}			
			

			return appList;
		}
		return null;
	}

	public List<SimpleAppointment> GetMyVisitingAppointments(String SecurityToken, int userId) {
		if(verifyUser(SecurityToken, userId)) {
			List<SimpleAppointment> appList = new ArrayList<SimpleAppointment>();
			
			for(Appointment a : sessions.get(SecurityToken).getVisitingAppointments()) {
				appList.add(a.toSimpleAppointment());
			}
			return appList;
		}
		return null;
	}

	public Location GetFriendLocation(String SecurityToken, int userId,
			int friendId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Location> GetFriendLocations(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> GetMessages(String SecurityToken, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean SendMessage(String SecurityToken, int userId, String message) {
		// TODO Auto-generated method stub
		return false;
	}

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
	public SimpleAppointment createAppointment(String SecurityToken, int userId,
			SimpleAppointment sAppointment) {
		// TODO Auto-generated method stub
		if(verifyUser(SecurityToken, userId)) {
			Appointment appointment = new Appointment(sAppointment);
			appointment.setHoster(sessions.get(SecurityToken));
			em.getTransaction().begin();
			em.persist(appointment);
			em.getTransaction().commit();
			return appointment.toSimpleAppointment();
		}
			
		return null;
	}



	@Override
	public SimpleUser getOwnData(String SecurityToken, int userId) {
		if(sessions.containsKey(SecurityToken)) {
			return sessions.get(SecurityToken).toSimpleUser();
		}
		return null;
	}



	@Override
	public SimpleUser createUser(String name, String password) {
		TypedQuery<User> results = em.createNamedQuery("User.findName", User.class).setParameter("name", name);
		try {
			results.getSingleResult();
			return null;

		} catch(NoResultException e) {
			CryptoService cryptoService = new CryptoService();
			SensitiveData sensitiveData = cryptoService.generatePassword(password);
			User newUser = new User();
			newUser.setUsername(name);
			newUser.setSalt(sensitiveData.getSalt());
			newUser.setPassword(sensitiveData.getEncryptedPassword());
			em.getTransaction().begin();
			em.persist(newUser);
			em.getTransaction().commit();	
			return newUser.toSimpleUser();			
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
				if(!app.getVisitingUsers().contains(usr)) {
					app.getVisitingUsers().add(usr);
				}
				if(!usr.getVisitingAppointments().contains(app.getId())) {
					usr.getVisitingAppointments().add(app);					
				}
				em.getTransaction().commit();
				return true;
			} catch(NoResultException e) {
				System.out.println("Appointment not found.");
			}
		}
		return false;
	}



	
	public boolean addFriend(String SecurityToken, int userId, int friendId) {
		// TODO Auto-generated method stub
		if(verifyUser(SecurityToken, userId)) {
			try {
				User usr = sessions.get(SecurityToken);
				TypedQuery<User> result = em.createNamedQuery("User.findId", User.class)
						.setParameter("id", friendId);
				User friend = result.getSingleResult();

				em.getTransaction().begin();
				if(!friend.getFriends().contains(usr)) {
					friend.getFriends().add(usr);
				}
				if(!usr.getFriends().contains(friend)) {
					usr.getFriends().add(friend);					
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
	public void inviteUsertoAppointment(String SecurityToken, int userId,
			int appointmentId, int inviteUserId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addFriendRequest(String SecurityToken, int userId,
			int friendId) {
		if(verifyUser(SecurityToken, userId)) {
			try {
				User usr = sessions.get(SecurityToken);
				TypedQuery<User> result = em.createNamedQuery("User.findId", User.class)
						.setParameter("id", friendId);
				User friend = result.getSingleResult();
								
				em.getTransaction().begin();
				if(!usr.getFriendInvites().contains(friend)) {
					UnconfirmedFriend request = new UnconfirmedFriend();
					request.setUser(usr);
					request.setFriend(friend);
					usr.getUnconfirmedFriendShips().add(request);
					em.persist(request);
				} else {
					System.out.println("Friend request already sent.");
				}
								
				em.getTransaction().commit();
				return true;
			} catch(NoResultException e) {
				System.out.println("Friend request failed.");
			}
		}
		return false;
	}

	@Override
	public boolean addFriendReply(String SecurityToken, int userId,
			int friendId, boolean accepted) {
		if(verifyUser(SecurityToken, userId)) {
			try {
				User usr = sessions.get(SecurityToken);
				TypedQuery<UnconfirmedFriend> result = em.createNamedQuery("Friend.findRequest", UnconfirmedFriend.class)
						.setParameter("userId", usr.getId())
						.setParameter("friendId", friendId);
				
				UnconfirmedFriend friend = result.getSingleResult();
				
				System.out.println("User friendrequests: " + usr.getFriendRequests().size());

				em.getTransaction().begin();
				em.remove(friend);
				em.getTransaction().commit();
				
				em.getTransaction().begin();
				if(accepted) {
					ConfirmedFriend confirmedFriend = new ConfirmedFriend();
					confirmedFriend.setUser(usr);
					confirmedFriend.setFriend(friend.getUser());
					
					ConfirmedFriend confirmedFriend2 = new ConfirmedFriend();
					confirmedFriend2.setUser(friend.getUser());
					confirmedFriend2.setFriend(usr);
					
					em.persist(confirmedFriend);
					em.persist(confirmedFriend2);
					
				}
				em.getTransaction().commit();
				
				return true;
			} catch(NoResultException e) {
				System.out.println("No request do confirm / deny.");
			}
		}
		return false;
	}
	
	public List<SimpleUser> getUsers(Set<Integer> ids) {
		TypedQuery<User> result = em.createNamedQuery("User.findIds", User.class).setParameter("userIds", ids);
		List<SimpleUser> users = new ArrayList<SimpleUser>(result.getResultList().size());
		for(User u : result.getResultList()) {
			users.add(u.toSimpleUser());
		}
		return null;
		
	}

	@Override
	public List<SimpleUser> getUsersByName(String subName) {
		TypedQuery<User> result = em.createNamedQuery("User.selectName", User.class).setParameter("subName", subName);
		List<SimpleUser> users = new ArrayList<SimpleUser>(result.getResultList().size());
		for(User u : result.getResultList()) {
			users.add(u.toSimpleUser());
		}	
		return users;
	}

}
