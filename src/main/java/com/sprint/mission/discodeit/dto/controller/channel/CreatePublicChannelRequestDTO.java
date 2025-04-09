package com.sprint.mission.discodeit.dto.controller.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePublicChannelRequestDTO(
    @NotBlank(message = "채널명을 입력해주세요.")
    @Size(min = 2, max = 12, message = "채널명은 2자 이상 12자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "채널명에는 특수문자를 포함할 수 없습니다.")
    String name,

    @Size(min = 1, max = 100, message = "채널 설명은 100글자 이내여야 합니다.")
    String description
) {

}