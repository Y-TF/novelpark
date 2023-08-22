package com.novelpark.presentation.support;

import com.novelpark.application.constant.AuthConstant;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class SessionUtil {

  private SessionUtil() {
    
  }

  public static void setSessionAttribute(
      HttpServletRequest request,
      HttpServletResponse response,
      long memberSeq
  ) {
    // 세션 생성
    HttpSession session = request.getSession(true);
    session.setAttribute(AuthConstant.SESSION_ATTR_NAME, memberSeq);
    session.setMaxInactiveInterval(AuthConstant.SESSION_TIMEOUT_IN_SECONDS);

    // 쿠키 생성 및 응답 헤더에 추가
    Cookie sessionCookie = new Cookie(AuthConstant.JSESSIONID, session.getId());
    sessionCookie.setHttpOnly(true);
    response.addCookie(sessionCookie);
  }
}
