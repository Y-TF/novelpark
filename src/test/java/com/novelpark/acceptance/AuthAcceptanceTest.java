package com.novelpark.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.DatabaseInitializer;
import com.novelpark.application.image.S3Uploader;
import com.novelpark.application.mail.MailService;
import com.novelpark.domain.image.ImageFile;
import com.novelpark.presentation.dto.request.auth.SignupRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthAcceptanceTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DatabaseInitializer databaseInitializer;

  @MockBean
  private S3Uploader s3Uploader;
  @MockBean
  private MailService mailService;

  @AfterEach
  void tearDown() {
    databaseInitializer.truncateTables();
  }

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

    @DisplayName("중복된 아이디가 주어지면 회원가입에 실패한다.")
    @Test
    void givenDuplicatedLoginId_whenSignup_thenFail() throws Exception {
      // given
      given(s3Uploader.uploadImageFile(any(ImageFile.class))).willReturn("image resource url");

      File stubFile = createStubFile();
      var request = requestWithSignupInfoAndProfileImage(stubFile);
      signup(request);

      // when
      var response = signup(request);

      // then
      assertThat(response.statusCode()).isEqualTo(400);
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

  @DisplayName("아이디를 찾을 때")
  @Nested
  class FindLoginId {

    @DisplayName("메일과 이름이 주어지면 아이디가 메일로 발송된다.")
    @Test
    void givenNameAndEmail_whenFindLoginId_thenSuccess() throws Exception {
      // given
      given(s3Uploader.uploadImageFile(any(ImageFile.class))).willReturn("image resource url");
      willDoNothing().given(mailService).sendFindLoginIdMail(anyString(), anyString());

      // 회원가입
      RestAssured
          .given()
          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
          .multiPart("request", objectMapper.writeValueAsString(
                  new SignupRequest("loginid", "asdf", "ParkJeongYong", "23Yong@novelpark.com")),
              MediaType.APPLICATION_JSON_VALUE)
          .multiPart("image", createStubFile(), MediaType.IMAGE_PNG_VALUE)
          .when().post("/api/signup")
          .then();

      var request = requestWithNameAndEmail();

      // when
      var response = findLoginId(request);

      // then
      assertThat(response.statusCode()).isEqualTo(200);
    }

    @DisplayName("존재하지 않는 메일이 주어지면 아이디를 찾는데 실패한다.")
    @Test
    void givenNameAndNotExists_whenFindLoginId_thenFail() {
      // given
      var request = requestWithNameAndEmail();

      // when
      var response = findLoginId(request);

      // then
      assertThat(response.statusCode()).isEqualTo(404);
    }

    private RequestSpecification requestWithNameAndEmail() {
      return RestAssured
          .given().log().all()
          .queryParam("name", "ParkJeongYong")
          .queryParam("email", "23Yong@novelpark.com");
    }

    private ExtractableResponse<Response> findLoginId(RequestSpecification request) {
      return request
          .when()
          .get("/api/id")
          .then().log().all()
          .extract();
    }
  }
}
