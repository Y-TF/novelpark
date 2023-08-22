package com.novelpark.domain.member.oauth;

import com.novelpark.application.auth.oauth.KakaoClient;
import com.novelpark.application.auth.oauth.NaverClient;
import com.novelpark.application.auth.oauth.OAuthClient;
import com.novelpark.config.properties.OauthProperties;
import com.novelpark.domain.member.Member;
import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.NotFoundException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {

  KAKAO("kakao") {
    @Override
    public String getLoginPageUrl(OauthProperties oauthProperties, String stateToken) {
      return oauthProperties.getKakao().getOAuthApiUrl() +
          "/authorize?" +
          "client_id=" + oauthProperties.getKakao().getClientId() +
          "&redirect_uri=" + oauthProperties.getKakao().getRedirectUrl() +
          "&response_type=code" +
          "&state=" + stateToken;
    }
  },
  NAVER("naver") {
    @Override
    public String getLoginPageUrl(OauthProperties oauthProperties, String stateToken) {
      return oauthProperties.getNaver().getOAuthApiUrl() +
          "/authorize?" +
          "client_id=" + oauthProperties.getNaver().getClientId() +
          "&response_type=code" +
          "&redirect_uri=" + oauthProperties.getNaver().getRedirectUrl() +
          "&state=" + stateToken;
    }
  };

  private final String name;

  private OAuthClient oAuthClient;

  public static OAuthProvider of(final String name) {
    return Arrays.stream(OAuthProvider.values())
        .filter(provider -> provider.name.equals(name))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND));
  }

  public static void validateOAuthProvider(OAuthProvider provider, Member member) {
    String exceptionMessage;
    if (member.getOAuthProvider() == null) {
      exceptionMessage = String.format("해당 이메일은 %s로 회원가입된 이력이 있습니다.", "아이디/비밀번호");
    } else {
      exceptionMessage =
          String.format("해당 이메일은 %s로 회원가입된 이력이 있습니다.", member.getOAuthProvider().name());
    }

    if (provider != member.getOAuthProvider()) {
      throw new BadRequestException(ErrorCode.NOT_MATCH_PROVIDER, exceptionMessage);
    }
  }

  public abstract String getLoginPageUrl(OauthProperties oauthProperties, String stateToken);

  private void injectOAuthClient(OAuthClient oAuthClient) {
    this.oAuthClient = oAuthClient;
  }

  @RequiredArgsConstructor
  @Component
  static class OAuthClientInjector {

    private final NaverClient naverClient;
    private final KakaoClient kakaoClient;

    @PostConstruct
    public void injectOAuthClient() {
      Arrays.stream(OAuthProvider.values()).forEach(oAuthProvider -> {
        if (oAuthProvider == NAVER) {
          oAuthProvider.injectOAuthClient(naverClient);
        }
        if (oAuthProvider == KAKAO) {
          oAuthProvider.injectOAuthClient(kakaoClient);
        }
      });
    }
  }
}
