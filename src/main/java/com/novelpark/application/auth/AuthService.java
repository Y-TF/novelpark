package com.novelpark.application.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.novelpark.application.image.ImageUploadService;
import com.novelpark.domain.member.Member;
import com.novelpark.domain.member.MemberRepository;
import com.novelpark.infrastructure.hash.PasswordEncoder;
import com.novelpark.presentation.dto.request.auth.SignupRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final ImageUploadService imageUploadService;

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Transactional
	public void signup(SignupRequest request, MultipartFile image) {
		String profileUrl = imageUploadService.uploadImage(image);

		memberRepository.save(Member.builder()
			.loginId(request.getId())
			.password(passwordEncoder.encrypt(request.getPassword()))
			.name(request.getName())
			.email(request.getEmail())
			.profileUrl(profileUrl)
			.build());
	}
}
