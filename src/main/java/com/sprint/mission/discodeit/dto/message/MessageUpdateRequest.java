package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.Size;

public record MessageUpdateRequest(
    @Size(max = 1000, message = "메시지 내용은 1000자를 초과할 수 없습니다.")
    String newContent
) {

}
