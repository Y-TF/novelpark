package com.novelpark.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// AUTH
	PASSWORD_ENCRYPT_FAIL("비밀번호 암호화에 실패");

	private String message;

	ErrorCode(String message) {
		this.message = message;
	}
}
