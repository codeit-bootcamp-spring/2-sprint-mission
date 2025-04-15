package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PaginationMapper {

    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalPages()
        );
    }

    public static <T> PaginatedResponse<T>  fromSlice(Slice<T> slice) {
        return new PaginatedResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                null
        );
    }
}
