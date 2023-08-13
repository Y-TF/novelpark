package com.novelpark.application.image;

import com.novelpark.domain.image.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageUploadService {

  private final S3Uploader s3Uploader;

  @Value("${s3.default.profile-url}")
  private String defaultProfileUrl;

  @Transactional
  public String uploadImage(MultipartFile image) {
    if (image == null || image.isEmpty()) {
      return defaultProfileUrl;
    }
    ImageFile imageFile = ImageFile.from(image);
    return s3Uploader.uploadImageFile(imageFile);
  }
}
