package com.spring.batch;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBatchExampleApplicationTests {

	@Test
	public void testPasswordEncryption() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setPassword("shahzad");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		String plainText = "passord";
		String encryptedPassword = encryptor.encrypt(plainText);
		System.out.println("EncryptedPassword : " + encryptedPassword);

		System.out.println("DecryptedPassword : " + encryptor.decrypt(encryptedPassword));
	}

	@Test
	public void testBasicPasswordEncryption() {
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		String plainText = "passord";
		String encryptedPassword = encryptor.encryptPassword(plainText);
		System.out.println("Basic Encrypted Password : " + encryptedPassword);
		if (encryptor.checkPassword(plainText, encryptedPassword)) {
			System.out.println("Password matched");
		} else {
			System.out.println("Invalid  Password");
		}
	}

	@Test
	public void StrongPasswordEncryptor() {
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		String plainText = "passord";
		String encryptedPassword = encryptor.encryptPassword(plainText);
		System.out.println("Strong Encrypted Password : " + encryptedPassword);
		if (encryptor.checkPassword(plainText, encryptedPassword)) {
			System.out.println("Password matched");
		} else {
			System.out.println("Invalid  Password");
		}
	}

}
