package com.novelpark.domain.member;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.novelpark.domain.AuditingFields;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends AuditingFields {

	private static final String DEFAULT_PROFILE_URL = "https://novel-park-bucket.s3.ap-northeast-2.amazonaws.com/public/profiles/default_profile.png";

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

	public Member(String loginId, String password, String name, String email) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.profileUrl = DEFAULT_PROFILE_URL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Member member = (Member)o;
		return Objects.equals(seq, member.seq);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}
}
