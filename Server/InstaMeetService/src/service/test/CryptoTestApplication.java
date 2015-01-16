package service.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;

import simpleEntities.SensitiveData;


public class CryptoTestApplication {

	public SensitiveData generatePassword(String password) {
		//generate salt
		SaltGenerator saltGen = new RandomSaltGenerator();
		byte [] salt = saltGen.generateSalt(8);

		//concat password+salt
		String saltString = null;
		String saltedPw = null;		
		ByteArrayOutputStream concatStream = new ByteArrayOutputStream();
		try {
			concatStream.write(password.getBytes());
			concatStream.write(salt);
			saltedPw = new String(concatStream.toByteArray(), "UTF-8");
			saltString = new String(salt, "UTF-8");
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	//First Time generation	
	String userPassword = "tes3tp"; //unencrypted

	SaltGenerator saltGen = new RandomSaltGenerator();
	byte [] salt = saltGen.generateSalt(8);
	
	//Concatenate salt+unencrypted password
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	try {
		outputStream.write(userPassword.getBytes());
		outputStream.write(salt);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Encrypt password
	StandardStringDigester digester = new StandardStringDigester();
	digester.setAlgorithm("SHA-1");   // optionally set the algorithm
	digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
	String saltedPw = null;
	try {
		 saltedPw = new String(outputStream.toByteArray(), "UTF-8");
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String encryptedPassword = digester.digest(saltedPw);
	
	
	
	//User input
	String inputPassword = "tes3tp";
	
	ByteArrayOutputStream inputEncryptStream = new ByteArrayOutputStream();
	try {
		inputEncryptStream.write(inputPassword.getBytes());
		inputEncryptStream.write(salt);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
	String inputSaltedPw = null;
	try {
		inputSaltedPw = new String(inputEncryptStream.toByteArray(), "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	System.out.println("Encrypted password: " + encryptedPassword);
	System.out.println("User password salted: " + saltedPw);
	System.out.println("User input salted: " + inputSaltedPw);

	digester = new StandardStringDigester();
	digester.setAlgorithm("SHA-1");   // optionally set the algorithm
	digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
	if (digester.matches(inputSaltedPw, encryptedPassword)) {
	  // correct!
		System.out.println("Login sucessful!");
	} else {
	  // bad login!
		System.out.println("Login failed!");
		
	}
	
	}

}
