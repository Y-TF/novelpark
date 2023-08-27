package com.novelpark.application.auth.oauth;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * OAuth 로그인시 상태토큰을 생성하는 클래스
 */
public final class StateGenerator {

  private static final SecureRandom random = new SecureRandom();

  private StateGenerator() {
  }

  public static String generateState() {
    return new BigInteger(130, random).toString(32);
  }

  public static boolean validateStateToken(String stateFromQueryParam, String stateFromSession) {
    return stateFromQueryParam.equals(stateFromSession);
  }
}