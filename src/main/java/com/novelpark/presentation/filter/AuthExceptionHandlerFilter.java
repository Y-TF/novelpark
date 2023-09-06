package com.novelpark.presentation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.exception.ErrorResponse;
import com.novelpark.exception.UnAuthorizedException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthExceptionHandlerFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (UnAuthorizedException ex) {
      setErrorResponse(response, ex);
    }
  }

  private void setErrorResponse(HttpServletResponse response,
      UnAuthorizedException ex) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");
    ErrorResponse errorResponse = ErrorResponse.from(ex);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
