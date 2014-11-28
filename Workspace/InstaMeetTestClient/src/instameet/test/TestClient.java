package instameet.test;

import instameet.client.InstaMeetService;
import instameet.client.LoginData;
import instameet.client.MInstaMeetService;
import instameet.client.User;

 
public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MInstaMeetService service =  new MInstaMeetService();
		InstaMeetService instaService = service.getInstaMeetServicePort();



		User newUser = instaService.createUser("hallo3", "welt");
		if(newUser != null) {
			newUser.getFriends();	
			System.out.println("Anzhal meiner Freude: " + newUser.getFriends().getEntry().size());
		}
		
		LoginData loginData = instaService.login("hallo3", "welt");
		instaService.addFriend(loginData.getToken(), loginData.getUserId(), 1);
		User myAcc = instaService.getOwnData(loginData.getToken(), loginData.getUserId());
	}

}
