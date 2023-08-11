package com.novelpark.exception;

import lombok.Getter;

@Getter
public class NovelParkException extends RuntimeException {

	private final ErrorCode errorCode;

	public NovelParkException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
