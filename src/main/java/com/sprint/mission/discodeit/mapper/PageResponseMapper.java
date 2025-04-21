package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.*;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface PageResponseMapper {

    default <E, T> PageResponse<T> fromPage(Page<E> page,
        Function<E, T> converter) {

        List<T> converted = page.stream()
            .map(converter)
            .toList();

        Object nextCursor = page.hasNext()
            ? page.getNumber() + 1
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

    default PageResponse<MessageDto> messageListToPageResponse(List<Message> messages, int size,
        boolean hasNext) {
        List<MessageDto> dtos = new ArrayList<>();

        for (Message message : messages) {
            dtos.add(messageToDto(message));
        }

        String nextCursor = hasNext && !messages.isEmpty() ?
            messages.get(messages.size() - 1).getCreatedAt().toString() : null;

        return PageResponse.<MessageDto>builder()
            .content(dtos)
            .nextCursor(nextCursor)
            .size(size)
            .hasNext(hasNext)
            .build();
    }

    MessageDto messageToDto(Message message);
}