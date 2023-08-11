package com.infraconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.novelpark.config.properties.AwsProperties;

@EnableConfigurationProperties(AwsProperties.class)
public class AwsConfig {
}
