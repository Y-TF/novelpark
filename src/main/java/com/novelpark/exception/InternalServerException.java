package com.novelpark.exception;

public class InternalServerException extends NovelParkException {

	public InternalServerException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
