package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public class PageResponseMapper {
    public static <E, D> PageResponse<D> fromSlice(Slice<E> slice, Function<E, D> mapper) {
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
