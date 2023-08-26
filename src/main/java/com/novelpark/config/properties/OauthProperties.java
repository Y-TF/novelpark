package com.novelpark.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("oauth")
public class OauthProperties {

  private final Naver naver;
  private final Kakao kakao;

  @ConstructorBinding
  public OauthProperties(Naver naver, Kakao kakao) {
    this.naver = naver;
    this.kakao = kakao;
  }

  @Getter
  @RequiredArgsConstructor
  public static class Naver {

    private final String clientId;
    private final String clientSecret;
    private final String oAuthApiUrl;
    private final String oAuthResourceUrl;
    private final String redirectUrl;
  }

  @Getter
  @RequiredArgsConstructor
  public static class Kakao {

    private final String clientId;
    private final String clientSecret;
    private final String oAuthApiUrl;
    private final String oAuthResourceUrl;
    private final String redirectUrl;
  }
}
