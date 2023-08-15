package com.novelpark.exception;

public class NotFoundException extends NovelParkException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
