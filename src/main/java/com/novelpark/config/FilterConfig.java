package com.novelpark.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.presentation.filter.AuthExceptionHandlerFilter;
import com.novelpark.presentation.filter.SessionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public FilterRegistrationBean<SessionFilter> jwtFilter() {
    FilterRegistrationBean<SessionFilter> jwtFilter = new FilterRegistrationBean<>();
    jwtFilter.setFilter(new SessionFilter());
    jwtFilter.addUrlPatterns("/api/*");
    jwtFilter.setOrder(2);
    return jwtFilter;
  }

  @Bean
  public FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter() {
    FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter = new FilterRegistrationBean<>();
    authExceptionHandlerFilter.setFilter(new AuthExceptionHandlerFilter(objectMapper));
    authExceptionHandlerFilter.addUrlPatterns("/api/*");
    authExceptionHandlerFilter.setOrder(1);
    return authExceptionHandlerFilter;
  }
}
