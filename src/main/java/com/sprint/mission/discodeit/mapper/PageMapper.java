package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageableResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public <T> PageResponseDto<T> fromPage(Page<T> page) {

        Object nextCursor = null;

        if (page.hasNext() && !page.getContent().isEmpty()) {
            T last = page.getContent().get(page.getContent().size() - 1);
            if (last instanceof MessageResponseDto message) {
                nextCursor = message.createdAt();
            }
        }

        return new PageResponseDto<>(
                page.getContent(),
                nextCursor,
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }


    // offset 방식
    public <T> PageableResponseDto<T> fromPage1(Page<T> page) {
        // 해당 방식 사용 시 fromPage 위부분 코드 복사 필요함
        return new PageableResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }

    // offset builder
    public <T> PageableResponseDto<T> fromPageOffset1(Page<T> page) {
        return PageableResponseDto.<T>builder()
                .content(page.getContent())
                .number(page.getNumber())
                .size(page.getSize())
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .build();
    }
}
