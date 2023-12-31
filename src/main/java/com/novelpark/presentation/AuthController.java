package com.novelpark.presentation;

import com.novelpark.application.auth.AuthService;
import com.novelpark.application.constant.AuthConstant;
import com.novelpark.presentation.dto.request.auth.LoginRequest;
import com.novelpark.presentation.dto.request.auth.SignupRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
      @Valid @RequestBody LoginRequest loginReq,
      HttpServletRequest servletReq,
      HttpServletResponse servletRes) {
    final long memberSeq = authService.login(loginReq);

    // 세션 생성
    HttpSession session = servletReq.getSession(true);
    session.setAttribute(AuthConstant.SESSION_ATTR_NAME, memberSeq);
    session.setMaxInactiveInterval(AuthConstant.SESSION_TIMEOUT_IN_SECONDS);

    // 쿠키 생성 및 응답 헤더에 추가
    Cookie sessionCookie = new Cookie(AuthConstant.JSESSIONID, session.getId());
    sessionCookie.setHttpOnly(true);
    servletRes.addCookie(sessionCookie);

    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/signup", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE
  })
  public ResponseEntity<Void> signup(@Valid @RequestPart SignupRequest request,
      @RequestPart MultipartFile image) {
    authService.signup(request, image);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
