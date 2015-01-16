package simpleEntities;

public class SensitiveData {

	String salt;
	String encryptedPassword;
	
	public SensitiveData(String salt, String encryptedPassword) {
		this.salt = salt;
		this.encryptedPassword = encryptedPassword;
	}
	
	public String getSalt() {
		return salt;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
}
