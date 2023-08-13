package com.novelpark.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // AUTH
  PASSWORD_ENCRYPT_FAIL("비밀번호 암호화 중 에러가 발생했습니다."),
  USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
  INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),

  // IMAGE
  INVALID_IMAGE("이미지 업로드에 실패했습니다."),
  INVALID_FILE_EXTENSION("이미지 파일의 확장자는 png, jpg, jpeg, gif만 가능합니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
