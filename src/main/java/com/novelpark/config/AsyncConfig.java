package com.novelpark.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

@EnableAsync
@Configuration(proxyBeanMethods = false)
public class AsyncConfig {

  @Bean(name = "mailThreadExecutor")
  public ThreadPoolExecutor mailExecutor() {
    return (ThreadPoolExecutor) Executors.newFixedThreadPool(2,
        new CustomizableThreadFactory("mail-thread-group"));
  }
}
