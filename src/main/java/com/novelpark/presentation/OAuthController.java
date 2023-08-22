package com.novelpark.presentation;

import com.novelpark.application.auth.OAuthService;
import com.novelpark.application.auth.oauth.StateGenerator;
import com.novelpark.domain.member.oauth.OAuthProvider;
import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import com.novelpark.presentation.support.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@RequestMapping("/api/login/OAuth/{provider}")
@RestController
public class OAuthController {

  private final OAuthService oAuthService;

  @GetMapping("/page")
  public ResponseEntity<String> redirectToLoginPage(
      @PathVariable OAuthProvider provider,
      HttpServletRequest request
  ) {
    // 상태토큰 설정
    HttpSession session = request.getSession(true);
    String stateToken = StateGenerator.generateState();
    session.setAttribute("state", stateToken);

    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, oAuthService.createRedirectUrl(provider, stateToken))
        .build();
  }

  @GetMapping
  public ResponseEntity<Void> oAuthLogin(
      @PathVariable OAuthProvider provider,
      @RequestParam("state") String stateFromQueryParam,
      @RequestParam String code,
      @SessionAttribute(value = "state", required = false) String stateFromSession,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    // 상태 토큰 검증
    if (!StateGenerator.validateStateToken(stateFromQueryParam, stateFromSession)) {
      throw new BadRequestException(ErrorCode.INVALID_STATE_TOKEN);
    }

    long memberId = oAuthService.oauthLogin(provider, code, stateFromQueryParam);
    SessionUtil.setSessionAttribute(request, response, memberId);

    return ResponseEntity.ok().build();
  }
}
