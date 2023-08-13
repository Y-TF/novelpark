package com.novelpark.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private final ErrorCode errorCode;
  private final String message;

  private ErrorResponse(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public static ErrorResponse from(NovelParkException ex) {
    return new ErrorResponse(ex.getErrorCode(), ex.getMessage());
  }
}
