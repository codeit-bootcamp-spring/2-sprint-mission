package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PageResponseMapper {
    public <T, R> PageResponse<R> toDto(Page<T> page, Function<T, R> converter){
        return new PageResponse<>(
                page.getContent().stream()
                        .map(converter)
                        .collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }

    public <T, R> PageResponse<R> toDto(Slice<T> slice, Function<T, R> converter){
        return new PageResponse<>(
                slice.getContent().stream()
                        .map(converter)
                        .collect(Collectors.toList()),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }
}
