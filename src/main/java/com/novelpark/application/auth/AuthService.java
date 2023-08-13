package com.novelpark.application.auth;

import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import com.novelpark.presentation.dto.request.auth.LoginRequest;
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

	@Transactional(readOnly = true)
	public long login(LoginRequest request) {
		Member member = memberRepository.findByLoginId(request.getLoginId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
		}

		return member.getSeq();
	}

	@Transactional
	public void signup(SignupRequest request, MultipartFile image) {
		String profileUrl = imageUploadService.uploadImage(image);

		memberRepository.save(Member.builder()
			.loginId(request.getLoginId())
			.password(passwordEncoder.encrypt(request.getPassword()))
			.name(request.getName())
			.email(request.getEmail())
			.profileUrl(profileUrl)
			.build());
	}
}
