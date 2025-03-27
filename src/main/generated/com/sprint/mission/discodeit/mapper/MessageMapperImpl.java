package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-28T02:43:44+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public CreateMessageParam toMessageParam(CreateMessageRequestDTO createMessageRequestDTO) {
        if ( createMessageRequestDTO == null ) {
            return null;
        }

        String content = null;
        UUID channelId = null;
        UUID authorId = null;

        content = createMessageRequestDTO.content();
        channelId = createMessageRequestDTO.channelId();
        authorId = createMessageRequestDTO.authorId();

        CreateMessageParam createMessageParam = new CreateMessageParam( content, channelId, authorId );

        return createMessageParam;
    }

    @Override
    public CreateMessageResponseDTO toMessageResponseDTO(MessageDTO messageDTO) {
        if ( messageDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        List<UUID> attachmentIds = null;
        String content = null;
        UUID channelId = null;
        UUID authorId = null;

        id = messageDTO.id();
        createdAt = messageDTO.createdAt();
        List<UUID> list = messageDTO.attachmentIds();
        if ( list != null ) {
            attachmentIds = new ArrayList<UUID>( list );
        }
        content = messageDTO.content();
        channelId = messageDTO.channelId();
        authorId = messageDTO.authorId();

        CreateMessageResponseDTO createMessageResponseDTO = new CreateMessageResponseDTO( id, createdAt, attachmentIds, content, channelId, authorId );

        return createMessageResponseDTO;
    }

    @Override
    public UpdateMessageParam toUpdateMessageParam(UpdateMessageRequestDTO updateMessageRequestDTO) {
        if ( updateMessageRequestDTO == null ) {
            return null;
        }

        String content = null;

        content = updateMessageRequestDTO.content();

        UpdateMessageParam updateMessageParam = new UpdateMessageParam( content );

        return updateMessageParam;
    }

    @Override
    public UpdateMessageResponseDTO toUpdateMessageResponseDTO(UpdateMessageDTO updateMessageDTO) {
        if ( updateMessageDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;
        List<UUID> attachmentIds = null;
        String content = null;
        UUID channelId = null;
        UUID authorId = null;

        id = updateMessageDTO.id();
        updatedAt = updateMessageDTO.updatedAt();
        List<UUID> list = updateMessageDTO.attachmentIds();
        if ( list != null ) {
            attachmentIds = new ArrayList<UUID>( list );
        }
        content = updateMessageDTO.content();
        channelId = updateMessageDTO.channelId();
        authorId = updateMessageDTO.authorId();

        UpdateMessageResponseDTO updateMessageResponseDTO = new UpdateMessageResponseDTO( id, updatedAt, attachmentIds, content, channelId, authorId );

        return updateMessageResponseDTO;
    }
}
