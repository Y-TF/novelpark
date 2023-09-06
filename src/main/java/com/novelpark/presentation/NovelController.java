package com.novelpark.presentation;

import com.novelpark.application.novel.NovelService;
import com.novelpark.presentation.dto.request.novel.NovelRegisterRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/novels")
@RestController
public class NovelController {

  private final NovelService novelService;

  @PostMapping(consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE
  })
  public ResponseEntity<Void> register(
      @Valid @RequestPart NovelRegisterRequest request,
      @RequestPart MultipartFile coverImage
  ) {
    novelService.register(request.getTitle(), coverImage);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
