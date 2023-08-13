package com.novelpark.config;

import com.novelpark.infrastructure.hash.PasswordEncoder;
import com.novelpark.infrastructure.hash.SHA256;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new SHA256();
  }
}
