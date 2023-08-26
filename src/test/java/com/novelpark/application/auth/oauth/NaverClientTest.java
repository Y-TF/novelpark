package com.novelpark.application.auth.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.novelpark.config.properties.OauthProperties;
import com.novelpark.config.properties.OauthProperties.Naver;
import com.novelpark.domain.member.oauth.OAuthUser;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

class NaverClientTest {

  private static final String TOKEN_RESPONSE = "{\n"
      + "    \"access_token\": \"AAAAQosjWDJieBiQZc3to9YQp6HDLvrmyKC+6+iZ3gq7qrkqf50ljZC+Lgoqrg\",\n"
      + "    \"refresh_token\": \"c8ceMEJisO4Se7uGisHoX0f5JEii7JnipglQipkOn5Zp3tyP7dHQoP0zNKHUq2gY\",\n"
      + "    \"token_type\": \"bearer\",\n"
      + "    \"expires_in\": \"3600\"\n"
      + "}";
  private static final String USER_RESPONSE = "{\n"
      + "  \"resultcode\": \"00\",\n"
      + "  \"message\": \"success\",\n"
      + "  \"response\": {\n"
      + "    \"email\": \"openapi@naver.com\",\n"
      + "    \"nickname\": \"OpenAPI\",\n"
      + "    \"profile_image\": \"https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif\",\n"
      + "    \"age\": \"40-49\",\n"
      + "    \"gender\": \"F\",\n"
      + "    \"id\": \"32742776\",\n"
      + "    \"name\": \"오픈 API\",\n"
      + "    \"birthday\": \"10-01\"\n"
      + "  }\n"
      + "}";
  private static final String ERROR_RESPONSE = "{\n"
      + "    \"error\": \"invalid_request\",\n"
      + "    \"error_description\": \"no valid data in session\"\n"
      + "}";

  @DisplayName("Naver 서버에 요청을 보내 인가코드로부터 유저의 정보를 가져오는데 성공한다.")
  @Test
  void givenMockWebServer_whenRequestNaverUserResoruce_thenSuccess() {
    try (MockWebServer mockOAuthServer = new MockWebServer()) {
      // given
      mockOAuthServer.start();
      setUpMockResponse(mockOAuthServer, TOKEN_RESPONSE);
      setUpMockResponse(mockOAuthServer, USER_RESPONSE);

      NaverClient naverClient = new NaverClient(new OauthProperties(new Naver(
          "clientId",
          "clientSecret",
          String.format("http://%s:%s", mockOAuthServer.getHostName(), mockOAuthServer.getPort()),
          String.format("http://%s:%s", mockOAuthServer.getHostName(), mockOAuthServer.getPort()),
          "redirectUrl"
      ), null), new RestTemplate());

      // when
      OAuthUser naverUser = naverClient.getOAuthUser("code", "stateToken");
      String email = naverUser.getEmail();

      // then
      assertThat(email).isEqualTo("openapi@naver.com");
      mockOAuthServer.shutdown();
    } catch (IOException ignored) {
    }
  }

  @DisplayName("Naver 서버에 요청을 보낼 때 access token을 가져오는데 실패한다.")
  @Test
  void givenInvalidInfo_whenRequestNaverUserResouce_thenFail() {
    try (MockWebServer mockOAuthServer = new MockWebServer()) {
      // given
      mockOAuthServer.start();
      setUpMockResponse(mockOAuthServer, ERROR_RESPONSE);

      NaverClient naverClient = new NaverClient(new OauthProperties(new Naver(
          "clientId",
          "clientSecret",
          String.format("http://%s:%s", mockOAuthServer.getHostName(), mockOAuthServer.getPort()),
          String.format("http://%s:%s", mockOAuthServer.getHostName(), mockOAuthServer.getPort()),
          "redirectUrl"
      ), null), new RestTemplate());

      // when & then
      assertThatThrownBy(() -> naverClient.getOAuthUser("code", "stateToken"))
          .isInstanceOf(InternalServerException.class)
          .extracting("errorCode").isEqualTo(ErrorCode.OAUTH_FAIL_REQUEST_TOKEN);

      mockOAuthServer.shutdown();
    } catch (IOException ignored) {
    }
  }

  private void setUpMockResponse(MockWebServer mockOAuthServer, String mockResponse) {
    mockOAuthServer.enqueue(new MockResponse()
        .setBody(mockResponse)
        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }
}
