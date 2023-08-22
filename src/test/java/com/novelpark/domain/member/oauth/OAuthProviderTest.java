package com.novelpark.domain.member.oauth;

import com.novelpark.application.auth.oauth.KakaoClient;
import com.novelpark.application.auth.oauth.NaverClient;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OAuthProviderTest {

  @DisplayName("OAuthProvider가 JVM에 로드될 때 OAuthClient 빈이 주입된다.")
  @Test
  void given_whenOAuthProviderLoadedOnJVM_thenInjectOAuthClientBean() {
    OAuthProvider naver = OAuthProvider.NAVER;
    OAuthProvider kakao = OAuthProvider.KAKAO;

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(naver.getOAuthClient()).isInstanceOf(NaverClient.class);
      softAssertions.assertThat(kakao.getOAuthClient()).isInstanceOf(KakaoClient.class);
    });
  }
}
