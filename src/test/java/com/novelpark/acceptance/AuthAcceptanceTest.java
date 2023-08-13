package com.novelpark.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.application.image.S3Uploader;
import com.novelpark.domain.image.ImageFile;
import com.novelpark.presentation.dto.request.auth.SignupRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthAcceptanceTest {

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private S3Uploader s3Uploader;

  private File createStubFile() throws IOException {
    File stubFile = File.createTempFile("test", ".png");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(stubFile))) {
      writer.write("content");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stubFile;
  }

  @DisplayName("회원가입 할 때")
  @Nested
  class Signup {

    @DisplayName("회원가입 정보와 프로필 이미지가 주어지면 회원가입에 성공한다.")
    @Test
    void givenSignupRequestAndProfileImage_whenSignup_thenSuccess() throws Exception {
      // given
      given(s3Uploader.uploadImageFile(any(ImageFile.class))).willReturn("image resource url");

      File stubFile = createStubFile();
      var request = requestWithSignupInfoAndProfileImage(stubFile);

      // when
      var response = signup(request);

      // then
      assertThat(response.statusCode()).isEqualTo(201);
    }

    private RequestSpecification requestWithSignupInfoAndProfileImage(File file) throws Exception {
      return RestAssured
          .given().log().all()
          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
          .multiPart("request",
              objectMapper.writeValueAsString(
                  new SignupRequest("loginid", "asdf", "jyp", "123@123")),
              MediaType.APPLICATION_JSON_VALUE)
          .multiPart("image", file, MediaType.IMAGE_PNG_VALUE);
    }

    private ExtractableResponse<Response> signup(RequestSpecification request) {
      return request
          .when()
          .post("/api/signup")
          .then().log().all()
          .extract();
    }
  }
}
