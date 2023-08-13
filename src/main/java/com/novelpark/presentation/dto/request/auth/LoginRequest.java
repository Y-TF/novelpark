package com.novelpark.presentation.dto.request.auth;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

  @NotBlank(message = "아이디는 빈 값이 들어올 수 없습니다.")
  private String loginId;
  @NotBlank(message = "비밀번호는 빈 값이 들어올 수 없습니다.")
  private String password;
}
