package com.sprint.mission.discodeit.service.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageableResponseDto<T> (
        List<T> content,
        int number,
        int size,
        Boolean hasNext,
        Long totalElements
) {
}
