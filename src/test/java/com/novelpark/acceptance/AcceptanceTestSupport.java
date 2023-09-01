package com.novelpark.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.SupportRepository;
import com.novelpark.application.image.S3Uploader;
import com.novelpark.application.mail.MailService;
import com.novelpark.domain.member.Member;
import com.novelpark.domain.member.oauth.OAuthProvider;
import com.novelpark.infrastructure.hash.PasswordEncoder;
import io.restassured.RestAssured;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@AcceptanceTest
public abstract class AcceptanceTestSupport {

  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected SupportRepository supportRepository;
  @MockBean
  protected S3Uploader s3Uploader;
  @MockBean
  protected MailService mailService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  protected File createStubFile() throws IOException {
    return File.createTempFile("test", ".png");
  }

  protected String signupAndGetSession() {
    supportRepository.save(Member.builder()
        .name("jeongyong")
        .email("23yong@naver.com")
        .loginId("23yong")
        .password(passwordEncoder.encrypt("asdf1234"))
        .oAuthProvider(OAuthProvider.NAVER)
        .profileUrl("url")
        .build());

    var sessionResponse = RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(Map.of("loginId", "23yong", "password", "asdf1234"))
        .when()
        .post("/api/login")
        .then().extract();
    return sessionResponse.sessionId();
  }
}
