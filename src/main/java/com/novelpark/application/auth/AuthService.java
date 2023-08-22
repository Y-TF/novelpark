package com.novelpark.application.auth;

import com.novelpark.application.image.ImageUploadService;
import com.novelpark.application.mail.MailService;
import com.novelpark.domain.member.Member;
import com.novelpark.domain.member.MemberRepository;
import com.novelpark.domain.member.oauth.OAuthProvider;
import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.NotFoundException;
import com.novelpark.infrastructure.hash.PasswordEncoder;
import com.novelpark.presentation.dto.request.auth.LoginRequest;
import com.novelpark.presentation.dto.request.auth.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final ImageUploadService imageUploadService;
  private final MailService mailService;

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
    if (memberRepository.existsByLoginId(request.getLoginId())) {
      throw new BadRequestException(ErrorCode.DUPLICATED_LOGIN_ID);
    }
    memberRepository.findByEmail(request.getEmail())
        .ifPresent(member -> OAuthProvider.validateOAuthProvider(null, member));

    String profileUrl = imageUploadService.uploadImage(image);

    memberRepository.save(Member.builder()
        .loginId(request.getLoginId())
        .password(passwordEncoder.encrypt(request.getPassword()))
        .name(request.getName())
        .email(request.getEmail())
        .profileUrl(profileUrl)
        .build());
  }

  @Transactional(readOnly = true)
  public void findLoginId(final String name, final String email) {
    String loginId = memberRepository.findLoginIdByEmailAndName(email, name)
        .orElseThrow(() -> new NotFoundException(
            ErrorCode.USER_NOT_FOUND,
            String.format("%s 이메일을 가진 사용자가 존재하지 않습니다.", email)
        ));

    mailService.sendFindLoginIdMail(loginId, email);
  }
}
