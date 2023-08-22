package com.novelpark.application.auth.oauth;

import com.novelpark.domain.member.oauth.OAuthUser;

public interface OAuthClient {

  OAuthUser getOAuthUser(String code, String stateToken);
}
