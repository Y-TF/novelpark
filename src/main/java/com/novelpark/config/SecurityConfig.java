package com.novelpark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.novelpark.infrastructure.hash.PasswordEncoder;
import com.novelpark.infrastructure.hash.SHA256;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SHA256();
	}
}
