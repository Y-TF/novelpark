package com.novelpark.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // AUTH
  PASSWORD_ENCRYPT_FAIL("비밀번호 암호화 중 에러가 발생했습니다."),
  DUPLICATED_LOGIN_ID("해당 아이디를 가진 회원이 이미 존재합니다."),
  INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
  NO_SESSION("세션이 존재하지 않습니다."),
  MAIL_AUTH_EXPIRED("인증 코드가 만료되었습니다."),
  WRONG_MAIL_AUTH("잘못된 인증 코드입니다."),
  UNAUTHORIZED("인증되지 않은 사용자입니다."),

  // OAUTH
  OAUTH_PROVIDER_NOT_FOUND("해당 OAuth 제공자가 존재하지 않습니다."),
  INVALID_STATE_TOKEN("상태 토큰이 일치하지 않습니다."),
  OAUTH_FAIL_REQUEST_TOKEN("토큰 발급에 실패했습니다."),
  NOT_MATCH_PROVIDER("해당 서비스의 로그인 기능은 제공하지 않습니다."),

  // IMAGE
  INVALID_IMAGE("이미지 업로드에 실패했습니다."),
  INVALID_FILE_EXTENSION("이미지 파일의 확장자는 png, jpg, jpeg, gif만 가능합니다."),

  // MAIL
  MAIL_SEND_FAIL("메일 전송에 실패했습니다."),

  // COMMON
  USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
  NOVEL_NOT_FOUND("해당 소설을 찾을 수 없습니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
