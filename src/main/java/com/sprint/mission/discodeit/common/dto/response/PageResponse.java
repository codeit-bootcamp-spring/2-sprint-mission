package com.sprint.mission.discodeit.common.dto.response;

import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements,
        Object nextCursor
) {

    public static <E, D> PageResponse<D> of(Slice<E> slice, Function<E, D> mapper, Object nextCursor) {
        List<D> content = slice.getContent().stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                content,
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null,
                nextCursor
        );
    }

}
