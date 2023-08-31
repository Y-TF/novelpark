package com.novelpark.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.novelpark.domain.image.ImageFile;
import io.restassured.RestAssured;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class NovelAcceptanceTest extends AcceptanceTestSupport {

  @DisplayName("소설 표지와 등록 정보가 주어지면 소설 등록에 성공한다.")
  @Test
  void givenNovelCoverImageAndRegisterData_whenRegisterNovel_thenSuccess() throws IOException {
    // given
    given(s3Uploader.uploadImageFile(any(ImageFile.class))).willReturn("url");

    var request = RestAssured
        .given().log().all()
        .sessionId("memberSeq", "1")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("request",
            Map.of("title", "던전에서 만남을 추구하면 안되는 걸까?"),
            MediaType.APPLICATION_JSON_VALUE)
        .multiPart("coverImage",
            createStubFile(),
            MediaType.IMAGE_PNG_VALUE);

    // when
    var response = request
        .when()
        .post("/api/novels")
        .then().log().all()
        .extract();

    // then
    assertThat(response.statusCode()).isEqualTo(201);
  }
}
