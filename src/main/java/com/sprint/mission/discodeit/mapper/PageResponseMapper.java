package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.PageResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Slice;
import java.util.List;

@Component
public class PageResponseMapper {

    public <E, T> PageResponse<T> fromPage(Page<E> page, Function<E, T> contentConverter) {
        List<T> content = page.getContent().stream()
            .map(contentConverter)
            .collect(Collectors.toList());
        return new PageResponse<>(
            content,
            page.getNumber(),
            page.getSize(),
            page.hasNext(),
            page.getTotalElements()
        );
    }

    public <E, T> PageResponse<T> fromSlice(Slice<E> slice, Function<E, T> contentConverter) {
        List<T> content = slice.getContent().stream()
            .map(contentConverter)
            .collect(Collectors.toList());

        return new PageResponse<>(
            content,
            slice.getNumber(),
            slice.getSize(),
            slice.hasNext(),
            null
        );
    }
}