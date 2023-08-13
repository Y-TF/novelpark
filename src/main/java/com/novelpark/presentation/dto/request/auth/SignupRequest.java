package com.novelpark.presentation.dto.request.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

  @NotBlank(message = "아이디는 빈 값이 들어올 수 없습니다.")
  @Size(max = 45)
  private String loginId;

  @NotBlank(message = "비밀번호는 빈 값이 들어올 수 없습니다.")
  private String password;

  @NotBlank(message = "이름은 빈 값이 들어올 수 없습니다.")
  @Size(max = 30, message = "이름의 길이는 최대 30자 입니다.")
  private String name;

  @NotBlank(message = "이메일은 빈 값이 들어올 수 없습니다.")
  @Size(max = 45, message = "이메일의 길이는 최대 45자입니다.")
  private String email;
}
