package com.novelpark.application.auth.oauth;

import com.novelpark.config.properties.OauthProperties;
import com.novelpark.config.properties.OauthProperties.Naver;
import com.novelpark.domain.member.oauth.NaverUser;
import com.novelpark.domain.member.oauth.OAuthUser;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverClient implements OAuthClient {

  private final RestTemplate restTemplate;
  private final String clientId;
  private final String clientSecret;
  private final String authorizationServerUrl;
  private final String resourceServerUrl;

  public NaverClient(OauthProperties oauthProperties, RestTemplate restTemplate) {
    Naver naverProperties = oauthProperties.getNaver();
    this.restTemplate = restTemplate;
    this.clientId = naverProperties.getClientId();
    this.clientSecret = naverProperties.getClientSecret();
    this.authorizationServerUrl = naverProperties.getOAuthApiUrl();
    this.resourceServerUrl = naverProperties.getOAuthResourceUrl();
  }

  @Override
  public OAuthUser getOAuthUser(final String code, final String stateToken) {
    String accessToken = requestAccessToken(code, stateToken);
    return requestUserResource(accessToken);
  }

  private String requestAccessToken(final String code, final String stateToken) {
    String url = UriComponentsBuilder.fromHttpUrl(authorizationServerUrl + "/token")
        .queryParam("grant_type", "authorization_code")
        .queryParam("client_id", clientId)
        .queryParam("client_secret", clientSecret)
        .queryParam("code", code)
        .queryParam("state", stateToken)
        .build()
        .toString();

    Map<String, Object> tokenResponse = restTemplate.postForObject(url, null, Map.class);
    validateToken(tokenResponse);
    return tokenResponse.get("access_token").toString();
  }

  private void validateToken(Map<String, Object> tokenResponse) {
    if (!tokenResponse.containsKey("access_token")) {
      throw new InternalServerException(
          ErrorCode.OAUTH_FAIL_REQUEST_TOKEN,
          tokenResponse.get("error_description").toString()
      );
    }
  }

  private OAuthUser requestUserResource(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    HttpEntity<Void> request = new HttpEntity<>(null, httpHeaders);

    Map<String, Object> response = restTemplate.postForObject(resourceServerUrl + "/me", request,
        Map.class);
    return new NaverUser(response);
  }
}
