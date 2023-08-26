package com.novelpark.application.auth;

import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class MailAuthStorage {

  private HashMap<Long, MailAuthDto> map = new HashMap<>();

  public void save(long memberSeq, String authCode) {
    map.put(memberSeq, new MailAuthDto(authCode));
  }

  public void checkCode(long memberSeq, String authCode) {
    MailAuthDto mailAuthDto = map.get(memberSeq);
    if (mailAuthDto.matches(authCode)) {
      throw new BadRequestException(ErrorCode.WRONG_MAIL_AUTH);
    }
    if (mailAuthDto.isExpired()) {
      throw new BadRequestException(ErrorCode.MAIL_AUTH_EXPIRED);
    }
  }
}
