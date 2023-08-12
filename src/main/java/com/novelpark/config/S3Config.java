package com.novelpark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.infraconfig.AwsConfig;
import com.novelpark.config.properties.AwsProperties;

@Import(AwsConfig.class)
@Configuration(proxyBeanMethods = false)
public class S3Config {

	@Bean
	public AmazonS3Client amazonS3Client(AwsProperties awsProperties) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsProperties.getCredentials().getAccessKey(),
			awsProperties.getCredentials().getSecretKey());

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(awsProperties.getRegion())
			.build();
	}
}
