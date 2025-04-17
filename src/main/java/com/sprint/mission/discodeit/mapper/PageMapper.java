package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.service.dto.response.PageableResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public <T> PageableResponseDto<T> fromPage(Page<T> page) {
        return new PageableResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }

    public <T> PageableResponseDto<T> fromPage1(Page<T> page) {
        return PageableResponseDto.<T>builder()
                .content(page.getContent())
                .number(page.getNumber())
                .size(page.getSize())
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .build();
    }
}
