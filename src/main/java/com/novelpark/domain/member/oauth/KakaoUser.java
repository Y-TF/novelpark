package com.novelpark.domain.member.oauth;

import java.util.Collections;
import java.util.Map;

public class KakaoUser implements OAuthUser {

  private final Map<String, Object> userInfo;

  public KakaoUser(Map<String, Object> userInfo) {
    this.userInfo = Collections.unmodifiableMap(userInfo);
  }

  @Override
  public String getEmail() {
    return getKakaoAccount().get("email").toString();
  }

  @Override
  public String getName() {
    return getProfile().get("nickname");
  }

  @Override
  public String getProfileUrl() {
    return getProfile().get("profile_image_url");
  }

  private Map<String, String> getProfile() {
    return (Map<String, String>) getKakaoAccount().get("profile");
  }

  private Map<String, Object> getKakaoAccount() {
    return (Map<String, Object>) userInfo.get("kakao_account");
  }
}
