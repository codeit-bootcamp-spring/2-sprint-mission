package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.PageResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Slice;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

    default <E, T> PageResponse<T> fromPage(Page<E> page,
        Function<E, T> converter) {

        List<T> converted = page.stream()
            .map(converter)
            .toList();

        Object nextCursor = page.hasNext()
            ? page.getNumber() + 1      // 필요에 따라 Cursor 산식 변경
            : null;

        return PageResponse.<T>builder()
            .content(converted)
            .nextCursor(nextCursor)
            .size(page.getSize())
            .hasNext(page.hasNext())
            .totalElements(page.getTotalElements())
            .build();
    }

    default <E, T> PageResponse<T> fromSlice(Slice<E> slice,
        Function<E, T> converter) {

        List<T> converted = slice.stream()
            .map(converter)
            .toList();

        Object nextCursor = slice.hasNext()
            ? slice.getNumber() + 1
            : null;

        return PageResponse.<T>builder()
            .content(converted)
            .nextCursor(nextCursor)
            .size(slice.getSize())
            .hasNext(slice.hasNext())
            .totalElements(null)
            .build();
    }
}