package com.novelpark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(ex));
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.from(ex));
  }
}
