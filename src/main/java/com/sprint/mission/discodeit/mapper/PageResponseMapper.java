package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

    public <T> PageResponse<T> fromSlice(Slice<T> slice) {
        Object nextCursor = null;

        List<T> content = slice.getContent();
        if (!content.isEmpty()) {
            T last = content.get(content.size() - 1);

            if (last instanceof MessageDto messageDto) {
                nextCursor = messageDto.createdAt();
            }
        }

        return new PageResponse<>(
            content,
            nextCursor,
            slice.getSize(),
            slice.hasNext(),
            null
        );
    }

    public <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            null,
            page.getSize(),
            page.hasNext(),
            page.getTotalElements()
        );
    }
}
