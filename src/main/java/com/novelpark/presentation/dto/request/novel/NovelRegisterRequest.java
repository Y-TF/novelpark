package com.novelpark.presentation.dto.request.novel;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NovelRegisterRequest {

  @NotBlank(message = "소설 제목은 빈 값이 들어올 수 없습니다.")
  private String title;
}
