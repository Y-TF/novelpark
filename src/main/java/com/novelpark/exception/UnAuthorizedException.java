package com.novelpark.exception;

public class UnAuthorizedException extends NovelParkException {

  public UnAuthorizedException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public UnAuthorizedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
