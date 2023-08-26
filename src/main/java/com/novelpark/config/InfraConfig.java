package com.novelpark.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan({"com.novelpark.config.properties"})
@Configuration
public class InfraConfig {

}
