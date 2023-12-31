package com.novelpark.exception;

public class BadRequestException extends NovelParkException {

  public BadRequestException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public BadRequestException(ErrorCode errorCode) {
    this(errorCode, errorCode.getMessage());
  }
}
