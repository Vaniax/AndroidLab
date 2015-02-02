package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.jasypt.digest.StandardByteDigester;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import simpleEntities.SensitiveData;

public class CryptoService {
	private final static String ALGORITHM = "SHA-256";
	private final static int ITERATIONS = 50000;
	private final static int SALT_SIZE = 8;
	private StandardStringDigester digester;
	
	public CryptoService() {
		digester = new StandardStringDigester();
		digester.setAlgorithm(ALGORITHM);
		digester.setIterations(ITERATIONS);
		digester.setSaltSizeBytes(SALT_SIZE);
	}
	

	
	public SensitiveData generatePassword(String password) {
		String encryptedPassword = digester.digest(password);
		return new SensitiveData(null, encryptedPassword);	// Explicit salt not necessary with jasypt
	}
	
	public boolean verifyLogin(String encryptedPassword, String salt, String inputPassword) {
		if (digester.matches(inputPassword, encryptedPassword)) {
			  // correct!
				return true;
			} else {
			  // bad login!
				return false;
			}
	}
	
	public String generateToken(int userID) {
		SaltGenerator saltGen = new RandomSaltGenerator();
		String uuid = new String();
		try {
			uuid = new String(saltGen.generateSalt(8),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is unknown"); // Should be impossible
		}
	
		String id = String.valueOf(userID);
		// One week token
		String expiringDate = LocalDateTime.now().plusDays(7).toString(); 
		
		StandardByteDigester digester = new StandardByteDigester();
		digester.setAlgorithm("SHA-256");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
		
		// Prevent usage of saving immutable string object with unencrypted content
		byte[] byteToken = digester.digest(String.join("|", uuid,id,expiringDate).getBytes());
		String token = Base64.encode(byteToken);
		
					
		return token;
	}
	
	public boolean isTokenExpired(String token) {
		Pattern pattern = Pattern.compile(Pattern.quote("|"));
		String[] tokenParts = pattern.split(token);
		
		LocalDateTime dateFromToken = LocalDateTime.parse(tokenParts[2]);
		if (LocalDateTime.now().isAfter(dateFromToken)) {
			return true;
		}
		return false;
	}
	
	public String decryptToken(String token) {
		StandardByteDigester digester = new StandardByteDigester();
		digester.setAlgorithm("SHA-256");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
		StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
		return enc.decrypt(token);
	}
	
	
	// Old wrong implementation
	public SensitiveData oldgeneratePassword(String password) {
		//generate salt
		SaltGenerator saltGen = new RandomSaltGenerator();
		byte [] salt = saltGen.generateSalt(8);

		//concat password+salt
		String saltString = null;
		String saltedPw = null;		
		ByteArrayOutputStream concatStream = new ByteArrayOutputStream();
		try {
			saltString = new String(salt, "UTF-8");
			concatStream.write(password.getBytes());
			concatStream.write(saltString.getBytes());
			saltedPw = new String(concatStream.toByteArray(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//Encrypt password
		StandardStringDigester digester = new StandardStringDigester();
		digester.setAlgorithm("SHA-256");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
		String encryptedPassword = digester.digest(saltedPw);

		
		return new SensitiveData(saltString, encryptedPassword);
		
	}
	
	
	public boolean oldverifyLogin(String encryptedPassword, String salt, String inputPassword) {
		String inputSaltedPw = null;
		ByteArrayOutputStream inputEncryptStream = new ByteArrayOutputStream();
		try {
			inputEncryptStream.write(inputPassword.getBytes());
			inputEncryptStream.write(salt.getBytes());
			inputSaltedPw = new String(inputEncryptStream.toByteArray(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		StandardStringDigester digester = new StandardStringDigester();
		digester.setAlgorithm("SHA-256");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
		if (digester.matches(inputSaltedPw, encryptedPassword)) {
		  // correct!
			return true;
		} else {
		  // bad login!
			return false;
		}
		
	}
}
