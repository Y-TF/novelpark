package com.novelpark.infrastructure.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;

public class SHA256 implements PasswordEncoder {

	@Override
	public String encrypt(final String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return bytesToHex(md.digest(password.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new InternalServerException(ErrorCode.PASSWORD_ENCRYPT_FAIL);
		}
	}

	@Override
	public boolean matches(String enteredPassword, String encryptedPassword) {
		return encrypt(enteredPassword).equals(encryptedPassword);
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder hexBuilder = new StringBuilder();
		for (byte b : bytes) {
			hexBuilder.append(String.format("%02x", b));
		}
		return hexBuilder.toString();
	}
}
