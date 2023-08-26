package com.novelpark.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(10)) // set connection timeout
        .setReadTimeout(Duration.ofSeconds(10))    // set read timeout
        .errorHandler(new CustomErrorHandler())   // add custom error handler
        .build();
  }

  private static class CustomErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
      log.error(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
    }
  }
}
