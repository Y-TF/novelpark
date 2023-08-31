package com.novelpark.application.novel;

import com.novelpark.application.image.ImageUploadService;
import com.novelpark.domain.novel.Novel;
import com.novelpark.domain.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class NovelService {

  private final NovelRepository novelRepository;
  private final ImageUploadService imageUploadService;

  @Transactional
  public void register(String title, MultipartFile coverImage) {
    String coverImageUrl = imageUploadService.uploadImage(coverImage);

    novelRepository.save(Novel.builder()
        .title(title)
        .coverImageUrl(coverImageUrl)
        .build());
  }
}
