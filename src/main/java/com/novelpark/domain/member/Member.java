package com.novelpark.domain.member;

import com.novelpark.domain.AuditingFields;
import com.novelpark.domain.member.oauth.OAuthProvider;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(length = 45, nullable = false)
  private String loginId;

  @Column(length = 512, nullable = false)
  private String password;

  @Column(length = 30, nullable = false)
  private String name;

  @Column(length = 45, nullable = false)
  private String email;

  @Column(length = 512, nullable = false)
  private String profileUrl;

  @Column(length = 45)
  @Enumerated(EnumType.STRING)
  private OAuthProvider OAuthProvider;

  @Builder
  public Member(Long seq, String loginId, String password, String name, String email,
      String profileUrl,
      OAuthProvider oAuthProvider) {
    this.seq = seq;
    this.loginId = loginId;
    this.password = password;
    this.name = name;
    this.email = email;
    this.profileUrl = profileUrl;
    this.OAuthProvider = oAuthProvider;
  }

  public void changePassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Member)) {
      return false;
    }
    Member member = (Member) o;
    return Objects.equals(seq, member.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }
}
