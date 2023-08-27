package com.novelpark.application.auth;

public interface MailAuthStorage {

  void save(long memberSeq, String authCode);

  void checkCode(long memberSeq, String authCode);
}
