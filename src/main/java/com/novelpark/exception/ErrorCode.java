package com.novelpark.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// AUTH
	PASSWORD_ENCRYPT_FAIL("비밀번호 암호화에 실패"),

	// IMAGE
	INVALID_IMAGE("이미지 업로드 실패"),
	INVALID_FILE_EXTENSION("유효하지 않은 파일 확장자");

	private String message;

	ErrorCode(String message) {
		this.message = message;
	}
}
