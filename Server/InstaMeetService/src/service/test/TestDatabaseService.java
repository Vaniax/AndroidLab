package service.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Appointment;

public class TestDatabaseService {

	
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("InstaMeetJPA");
		EntityManager em = factory.createEntityManager();
		
		Map<Integer, Double> appointmentDistanceTable = new HashMap<Integer, Double>();
		
		double longitude = 3.3;
		double lattitude = 5.1;
		
		Query qs = em.createNativeQuery("SELECT a.id, ( 3959 * acos(cos(radians(" + lattitude +")) * cos( radians(a.lattitude)) * cos( radians(a.longitude) - radians(" + longitude + ")) + sin(radians(" + lattitude + ")) * sin(radians(a.lattitude)))) AS distance FROM appointments a HAVING distance < 10000 ORDER BY distance LIMIT 0 , 20 ");
		Query q = em.createQuery("SELECT a, (3959 * FUNC('acos',FUNC('cos',FUNC('radians',:lat))*FUNC('cos',FUNC('radians',a.lattitude))*FUNC('cos',FUNC('radians',a.longitude)-FUNC('radians',:lon)) + FUNC('sin',FUNC('radians',:lat)) * FUNC('sin',FUNC('radians',a.lattitude)) )) AS distance FROM Appointment a HAVING distance < 10000 ORDER BY distance")
				.setParameter("lat", lattitude)
				.setParameter("lon", longitude)
				.setMaxResults(50);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = q.getResultList();
		for(Object[] obj : list) {
			appointmentDistanceTable.put(((Appointment)obj[0]).getId(), (double)obj[1]);
		}
		
		for(int id : appointmentDistanceTable.keySet()) {
			System.out.println("Appointment ID: " + id + "\t Distance: " + appointmentDistanceTable.get(id));
		}
	}
}
