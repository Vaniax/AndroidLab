package service.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;


public class CryptoTestApplication {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	//First Time generation	
	String userPassword = "test"; //unencrypted

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
	String encryptedPassword = digester.digest(outputStream.toByteArray().toString());
	
	
	
	//User input
	String inputPassword = "test";
	
	ByteArrayOutputStream inputEncryptStream = new ByteArrayOutputStream();
	try {
		inputEncryptStream.write(inputPassword.getBytes());
		inputEncryptStream.write(salt);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	

	
	
	System.out.println("Encrypted password: " + encryptedPassword);
	System.out.println("User password salted: " + outputStream.toByteArray().toString());
	System.out.println("User input salted: " + inputEncryptStream.toByteArray().toString());

	digester = new StandardStringDigester();
	digester.setAlgorithm("SHA-1");   // optionally set the algorithm
	digester.setIterations(50000);  // increase security by performing 50000 hashing iterations
	if (digester.matches(inputEncryptStream.toByteArray().toString(), encryptedPassword)) {
	  // correct!
		System.out.println("Login sucessful!");
	} else {
	  // bad login!
		System.out.println("Login failed!");
		
	}
	
	}

}
