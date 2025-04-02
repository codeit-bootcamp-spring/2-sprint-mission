package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-02T10:45:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
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

        id = updateMessageDTO.id();
        updatedAt = updateMessageDTO.updatedAt();
        List<UUID> list = updateMessageDTO.attachmentIds();
        if ( list != null ) {
            attachmentIds = new ArrayList<UUID>( list );
        }
        content = updateMessageDTO.content();
        channelId = updateMessageDTO.channelId();

        UserResponseDTO userResponseDTO = null;

        UpdateMessageResponseDTO updateMessageResponseDTO = new UpdateMessageResponseDTO( id, updatedAt, attachmentIds, content, channelId, userResponseDTO );

        return updateMessageResponseDTO;
    }

    @Override
    public MessageDTO toMessageDTO(Message message, UserDTO userDTO) {
        if ( message == null && userDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<UUID> attachmentIds = null;
        String content = null;
        UUID channelId = null;
        if ( message != null ) {
            id = message.getId();
            createdAt = message.getCreatedAt();
            updatedAt = message.getUpdatedAt();
            List<UUID> list = message.getAttachmentIds();
            if ( list != null ) {
                attachmentIds = new ArrayList<UUID>( list );
            }
            content = message.getContent();
            channelId = message.getChannelId();
        }
        UserDTO userDTO1 = null;
        userDTO1 = userDTO;

        MessageDTO messageDTO = new MessageDTO( id, createdAt, updatedAt, attachmentIds, content, channelId, userDTO1 );

        return messageDTO;
    }

    @Override
    public UpdateMessageDTO toUpdateMessageDTO(Message message, UserDTO userDTO) {
        if ( message == null && userDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;
        List<UUID> attachmentIds = null;
        String content = null;
        UUID channelId = null;
        if ( message != null ) {
            id = message.getId();
            updatedAt = message.getUpdatedAt();
            List<UUID> list = message.getAttachmentIds();
            if ( list != null ) {
                attachmentIds = new ArrayList<UUID>( list );
            }
            content = message.getContent();
            channelId = message.getChannelId();
        }
        UserDTO userDTO1 = null;
        userDTO1 = userDTO;

        UpdateMessageDTO updateMessageDTO = new UpdateMessageDTO( id, updatedAt, attachmentIds, content, channelId, userDTO1 );

        return updateMessageDTO;
    }
}
