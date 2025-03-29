package com.sprint.mission.discodeit.dto.controller.binarycontent;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record FindAllBinaryContentInRequestDTO(
        @NotNull(message = "attachmentId는 null일 수 없습니다.")
        @Size(min = 1, message = "최소 한 개 이상의 attachmentId를 입력해주세요.")
        List<UUID> attachmentIds
) {
}
