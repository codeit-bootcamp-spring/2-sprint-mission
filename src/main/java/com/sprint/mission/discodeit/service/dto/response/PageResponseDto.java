package com.sprint.mission.discodeit.service.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponseDto<T> (
        List<T> content,
        Object nextCursor,
        int size,
        Boolean hasNext,
        Long totalElements
) {
}
