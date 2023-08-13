package com.novelpark.presentation.dto.request.auth;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
  @NotBlank
  private String loginId;
  @NotBlank
  private String password;

}
