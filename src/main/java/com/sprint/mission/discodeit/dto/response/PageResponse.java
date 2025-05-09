package com.sprint.mission.discodeit.dto.response;

import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements) {

    public static <E, D> PageResponse<D> of(Slice<E> slice, Function<E, D> mapper) {
        List<D> content = slice.getContent().stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                content,
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }
}
