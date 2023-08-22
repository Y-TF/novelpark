package com.novelpark.presentation.converter;

import com.novelpark.domain.member.oauth.OAuthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OAuthProviderRequestConverter implements Converter<String, OAuthProvider> {

  @Override
  public OAuthProvider convert(String oauthProvider) {
    return OAuthProvider.of(oauthProvider);
  }
}
