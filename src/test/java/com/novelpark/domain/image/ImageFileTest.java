package com.novelpark.domain.image;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;

class ImageFileTest {

  @DisplayName("올바른 MultipartFile 이 들어와 ImageFile 생성에 성공한다.")
  @Test
  void givenMultipartFile_whenCreateImageFile_thenSuccess() {
    // given
    var mockMultipartFile = new MockMultipartFile(
        "test-image",
        "image.png",
        MediaType.IMAGE_PNG_VALUE,
        "imaegBytes".getBytes(StandardCharsets.UTF_8)
    );

    // when & then
    assertThatCode(() -> ImageFile.from(mockMultipartFile)).doesNotThrowAnyException();
  }

  @DisplayName("이미지 파일이 아닌 확장자가 주어지면 ImageFile 생성에 실패한다.")
  @Test
  void givenInvalidFileExtensionMultipartFile_whenCreateImageFile_thenThrowsException() {
    // given
    var mockMultipartFile = new MockMultipartFile(
        "test-image",
        "image.pdf",
        MediaType.IMAGE_PNG_VALUE,
        "imaegBytes".getBytes(StandardCharsets.UTF_8)
    );

    // when & then
    assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
        .isInstanceOf(BadRequestException.class)
        .extracting("errorCode").isEqualTo(ErrorCode.INVALID_FILE_EXTENSION);
  }
}
