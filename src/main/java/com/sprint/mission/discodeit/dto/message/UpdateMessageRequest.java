package com.sprint.mission.discodeit.dto.message;


import jakarta.validation.constraints.NotBlank;

public record UpdateMessageRequest(
    @NotBlank(message = "수정할 메시지 내용은 비어 있을 수 없습니다.")
    String newContent
) {

}
