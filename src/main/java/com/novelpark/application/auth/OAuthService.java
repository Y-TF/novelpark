package com.novelpark.application.auth;

import com.novelpark.application.auth.oauth.OAuthClient;
import com.novelpark.config.properties.OauthProperties;
import com.novelpark.domain.member.Member;
import com.novelpark.domain.member.MemberRepository;
import com.novelpark.domain.member.oauth.OAuthProvider;
import com.novelpark.domain.member.oauth.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OAuthService {

  private final OauthProperties oauthProperties;
  private final MemberRepository memberRepository;

  public String createRedirectUrl(OAuthProvider oAuthProvider, final String stateToken) {
    return oAuthProvider.getLoginPageUrl(oauthProperties, stateToken);
  }

  @Transactional
  public long oauthLogin(OAuthProvider provider, final String code, final String stateToken) {
    OAuthClient oAuthClient = provider.getOAuthClient();
    OAuthUser oAuthUser = oAuthClient.getOAuthUser(code, stateToken);

    String email = oAuthUser.getEmail();

    Member member = memberRepository.findByEmail(email)
        .orElseGet(() -> Member.builder()
            .loginId("")
            .password("")
            .email(email)
            .name(oAuthUser.getName())
            .profileUrl(oAuthUser.getProfileUrl())
            .oAuthProvider(provider)
            .build()
        );

    OAuthProvider.validateOAuthProvider(provider, member);

    // 기존 회원인 경우 로그인 성공
    if (member.getSeq() != null) {
      return member.getSeq();
    }
    // 기존 회원이 아니라면 강제 회원가입
    return memberRepository.save(member).getSeq();
  }
}
