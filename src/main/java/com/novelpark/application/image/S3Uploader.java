package com.novelpark.application.image;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.novelpark.config.properties.AwsProperties;
import com.novelpark.domain.image.ImageFile;

@Component
public class S3Uploader {

	private static final String PROFILE_IMAGE_DIR = "public/profiles/";

	private final AmazonS3Client s3Client;
	private final String bucket;

	public S3Uploader(AmazonS3Client s3Client, AwsProperties awsProperties) {
		this.s3Client = s3Client;
		this.bucket = awsProperties.getS3().getBucket();
	}

	public String uploadImageFile(ImageFile imageFile) {
		final String fileName = PROFILE_IMAGE_DIR + imageFile.getRandomName();
		putS3(imageFile, fileName);
		return getObjectUrl(fileName);
	}

	private void putS3(ImageFile imageFile, final String fileName) {
		s3Client.putObject(new PutObjectRequest(
			bucket,
			fileName,
			imageFile.getInputStream(),
			createObjectMetaData(imageFile))
			.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private ObjectMetadata createObjectMetaData(ImageFile imageFile) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getFileSize());
		return metadata;
	}

	private String getObjectUrl(final String fileName) {
		return URLDecoder.decode(s3Client.getUrl(bucket, fileName).toString(), StandardCharsets.UTF_8);
	}
}
