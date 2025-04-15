package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateResponseDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MessageMapperV1 {

    @Mapping(source = "content", target = "content")
    MessageResponseDto toMessageResponseDto(Message message);

    @Mapping(source = "lastModified", target = "updateAt")
    @Mapping(source = "content", target = "content")
    MessageUpdateResponseDto toMessageUpdateResponseDto(Message message);


}
