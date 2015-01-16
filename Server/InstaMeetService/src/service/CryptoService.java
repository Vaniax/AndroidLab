package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;

import simpleEntities.SensitiveData;

public class CryptoService {
	public CryptoService() {
	}
	
	public SensitiveData generatePassword(String password) {
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
		digester.setAlgorithm("SHA-1");   // optionally set the algorithm
		digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
		String encryptedPassword = digester.digest(saltedPw);

		
		return new SensitiveData(saltString, encryptedPassword);
		
	}
	
	
	public boolean verifyLogin(String encryptedPassword, String salt, String inputPassword) {
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
		digester.setAlgorithm("SHA-1");   // optionally set the algorithm
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
