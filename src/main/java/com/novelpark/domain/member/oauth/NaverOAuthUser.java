package com.novelpark.domain.member.oauth;

import java.util.Collections;
import java.util.Map;

public class NaverOAuthUser implements OAuthUser {

  private final Map<String, Object> userInfo;

  public NaverOAuthUser(Map<String, Object> userInfo) {
    this.userInfo = Collections.unmodifiableMap(userInfo);
  }

  @Override
  public String getEmail() {
    return getResponse().get("email");
  }

  @Override
  public String getName() {
    return getResponse().get("name");
  }

  @Override
  public String getProfileUrl() {
    return getResponse().get("profile_image");
  }

  private Map<String, String> getResponse() {
    return (Map<String, String>) userInfo.get("response");
  }
}
