package com.novelpark.presentation.filter;

import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.UnAuthorizedException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionFilter extends OncePerRequestFilter {

  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final List<String> excludeUrlPatterns =
      List.of("/api/login/**", "/api/signup", "/api/id", "/api/password/reset/**");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return excludeUrlPatterns.stream()
        .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (CorsUtils.isPreFlightRequest(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    verifyMemberIsLogin(request);

    filterChain.doFilter(request, response);
  }

  private void verifyMemberIsLogin(HttpServletRequest request) {
    Optional.ofNullable(request.getSession(false))
        .orElseThrow(() -> new UnAuthorizedException(ErrorCode.UNAUTHORIZED));
  }
}
