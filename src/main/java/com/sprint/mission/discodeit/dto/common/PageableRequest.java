package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PageableRequest {

  @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
  private int page = 0;

  @Positive(message = "페이지 크기는 양수여야 합니다.")
  private int size = 50;

  private String sort = "createdAt, desc";
}
