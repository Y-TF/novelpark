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
			md.update(password.getBytes());
			return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException | NullPointerException e) {
			throw new InternalServerException(ErrorCode.PASSWORD_ENCRYPT_FAIL, "비밀번호 암호화 중 에러가 발생했습니다.");
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder hexBuilder = new StringBuilder();
		for (byte b : bytes) {
			hexBuilder.append(String.format("%02x", b));
		}
		return hexBuilder.toString();
	}
}
