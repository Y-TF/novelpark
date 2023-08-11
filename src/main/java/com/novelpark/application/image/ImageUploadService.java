package com.novelpark.application.image;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.novelpark.domain.image.ImageFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageUploadService {

	private static final String DEFAULT_PROFILE_URL = "https://novel-park-bucket.s3.ap-northeast-2.amazonaws.com/public/profiles/default_profile.png";

	private final S3Uploader s3Uploader;

	@Transactional
	public String uploadImage(MultipartFile image) {
		if (image == null || image.isEmpty()) {
			return DEFAULT_PROFILE_URL;
		}
		ImageFile imageFile = ImageFile.from(image);
		return s3Uploader.uploadImageFile(imageFile);
	}
}
