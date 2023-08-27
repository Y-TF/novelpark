package com.novelpark.application.auth;

import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryMailAuthStorage implements MailAuthStorage {

  private HashMap<Long, MailAuthVo> storage = new HashMap<>();

  @Override
  public void save(long memberSeq, String authCode) {
    storage.put(memberSeq, new MailAuthVo(authCode));
  }

  @Override
  public void checkCode(long memberSeq, String authCode) {
    MailAuthVo mailAuthVo = storage.get(memberSeq);
    if (mailAuthVo.matches(authCode)) {
      throw new BadRequestException(ErrorCode.WRONG_MAIL_AUTH);
    }
    if (mailAuthVo.isExpired()) {
      throw new BadRequestException(ErrorCode.MAIL_AUTH_EXPIRED);
    }
  }
}
