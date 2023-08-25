package com.novelpark.application.auth.oauth;

import com.novelpark.config.properties.OauthProperties;
import com.novelpark.config.properties.OauthProperties.Kakao;
import com.novelpark.domain.member.oauth.KakaoOAuthUser;
import com.novelpark.domain.member.oauth.OAuthUser;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoClient implements OAuthClient {

  private final RestTemplate restTemplate;
  private final String clientId;
  private final String clientSecret;
  private final String authorizationServerUrl;
  private final String resourceServerUrl;
  private final String redirectUrl;

  public KakaoClient(OauthProperties oauthProperties, RestTemplate restTemplate) {
    Kakao kakaoProperties = oauthProperties.getKakao();
    this.restTemplate = restTemplate;
    this.clientId = kakaoProperties.getClientId();
    this.clientSecret = kakaoProperties.getClientSecret();
    this.authorizationServerUrl = kakaoProperties.getOAuthApiUrl();
    this.resourceServerUrl = kakaoProperties.getOAuthResourceUrl();
    this.redirectUrl = kakaoProperties.getRedirectUrl();
  }

  @Override
  public OAuthUser getOAuthUser(final String code, final String stateToken) {
    String accessToken = requestAccessToken(code);
    return requestUserResource(accessToken);
  }

  private String requestAccessToken(final String code) {
    String url = UriComponentsBuilder.fromHttpUrl(authorizationServerUrl + "/token")
        .build()
        .toString();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    var request = new HttpEntity<>(createTokenRequestBody(code), httpHeaders);

    Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
    validateToken(response);
    return response.get("access_token").toString();
  }

  private MultiValueMap<String, String> createTokenRequestBody(final String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", clientId);
    params.add("redirect_uri", redirectUrl);
    params.add("code", code);
    params.add("client_secret", clientSecret);
    return params;
  }

  private void validateToken(Map<String, Object> tokenResponse) {
    if (!tokenResponse.containsKey("access_token")) {
      throw new InternalServerException(
          ErrorCode.OAUTH_FAIL_REQUEST_TOKEN,
          tokenResponse.get("error_description").toString()
      );
    }
  }

  private KakaoOAuthUser requestUserResource(final String token) {
    String url = UriComponentsBuilder.fromHttpUrl(resourceServerUrl + "/me")
        .queryParam("property_keys",
            "[\"kakao_account.profile\", \"kakao_account.email\", \"kakao_account.name\"]")
        .build()
        .toString();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setBearerAuth(token);

    HttpEntity<Void> request = new HttpEntity<>(null, httpHeaders);

    Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
    return new KakaoOAuthUser(response);
  }
}
