package com.novelpark.application.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.novelpark.domain.image.ImageFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageUploadService {

	@Value("${url.profile}")
	private String defaultProfileUrl;

	private final S3Uploader s3Uploader;

	@Transactional
	public String uploadImage(MultipartFile image) {
		if (image == null || image.isEmpty()) {
			return defaultProfileUrl;
		}
		ImageFile imageFile = ImageFile.from(image);
		return s3Uploader.uploadImageFile(imageFile);
	}
}
