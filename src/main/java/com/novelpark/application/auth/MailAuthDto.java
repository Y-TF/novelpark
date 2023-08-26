package com.novelpark.application.auth;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MailAuthDto {
  private String authCode;
  private LocalDateTime expireDt;

  public MailAuthDto(String authCode) {
    this.authCode = authCode;
    this.expireDt = LocalDateTime.now().plusMinutes(30);
  }

  public boolean matches(String authCode) {
    return this.authCode.equals(authCode);
  }

  public boolean isExpired() {
    return this.expireDt.isBefore(LocalDateTime.now());
  }
}
