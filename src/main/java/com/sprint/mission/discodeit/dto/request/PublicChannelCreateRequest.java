package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequest(
    @NotBlank(message = "name이 공백일 수 없습니다.")
    String name,
    String description
) {

  // 커스텀 생성자 정의
  public PublicChannelCreateRequest(String name, String description) {
    this.name = name;
    this.description = (description == null) ? "" : description;
  }
}
