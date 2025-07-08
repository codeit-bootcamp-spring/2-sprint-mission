package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserMapper.class})
public interface PageResponseMapper {


    default PageResponse<MessageDto> messageListToPageResponse(List<Message> messages, int size,
        boolean hasNext) {
        List<MessageDto> dtos = messages.stream()
            .map(this::messageToDto)
            .collect(Collectors.toList());

        String nextCursor = hasNext && !messages.isEmpty() ?
            messages.get(messages.size() - 1).getCreatedAt().toString() : null;

        return PageResponse.<MessageDto>builder()
            .content(dtos)
            .nextCursor(nextCursor)
            .size(size)
            .hasNext(hasNext)
            .totalElements((long) dtos.size())
            .build();
    }

    @Mapping(source = "channel.id", target = "channelId")
    @Mapping(source = "author", target = "author")
    MessageDto messageToDto(Message message);
}