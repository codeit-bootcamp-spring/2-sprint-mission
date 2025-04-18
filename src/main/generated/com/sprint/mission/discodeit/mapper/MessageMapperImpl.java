package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.FindMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-18T23:43:23+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public CreateMessageCommand toCreateMessageCommand(CreateMessageRequestDTO createMessageRequestDTO) {
        if ( createMessageRequestDTO == null ) {
            return null;
        }

        String content = null;
        UUID channelId = null;
        UUID authorId = null;

        content = createMessageRequestDTO.content();
        channelId = createMessageRequestDTO.channelId();
        authorId = createMessageRequestDTO.authorId();

        CreateMessageCommand createMessageCommand = new CreateMessageCommand( content, channelId, authorId );

        return createMessageCommand;
    }

    @Override
    public CreateMessageResponseDTO toCreateMessageResponseDTO(CreateMessageResult createMessageResult) {
        if ( createMessageResult == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        UUID channelId = null;
        FindUserResult author = null;

        id = createMessageResult.id();
        createdAt = createMessageResult.createdAt();
        updatedAt = createMessageResult.updatedAt();
        List<FindBinaryContentResult> list = createMessageResult.attachments();
        if ( list != null ) {
            attachments = new ArrayList<FindBinaryContentResult>( list );
        }
        content = createMessageResult.content();
        channelId = createMessageResult.channelId();
        author = createMessageResult.author();

        CreateMessageResponseDTO createMessageResponseDTO = new CreateMessageResponseDTO( id, createdAt, updatedAt, attachments, content, channelId, author );

        return createMessageResponseDTO;
    }

    @Override
    public UpdateMessageCommand toUpdateMessageCommand(UpdateMessageRequestDTO updateMessageRequestDTO) {
        if ( updateMessageRequestDTO == null ) {
            return null;
        }

        String newContent = null;

        newContent = updateMessageRequestDTO.newContent();

        UpdateMessageCommand updateMessageCommand = new UpdateMessageCommand( newContent );

        return updateMessageCommand;
    }

    @Override
    public UpdateMessageResponseDTO toUpdateMessageResponseDTO(UpdateMessageResult updateMessageResult) {
        if ( updateMessageResult == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        UUID channelId = null;
        FindUserResult author = null;

        id = updateMessageResult.id();
        createdAt = updateMessageResult.createdAt();
        updatedAt = updateMessageResult.updatedAt();
        List<FindBinaryContentResult> list = updateMessageResult.attachments();
        if ( list != null ) {
            attachments = new ArrayList<FindBinaryContentResult>( list );
        }
        content = updateMessageResult.content();
        channelId = updateMessageResult.channelId();
        author = updateMessageResult.author();

        UpdateMessageResponseDTO updateMessageResponseDTO = new UpdateMessageResponseDTO( id, createdAt, updatedAt, attachments, content, channelId, author );

        return updateMessageResponseDTO;
    }

    @Override
    public FindMessageResponseDTO toFindMessageResponseDTO(FindMessageResult findMessageResult) {
        if ( findMessageResult == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        UUID channelId = null;
        FindUserResult author = null;

        id = findMessageResult.id();
        createdAt = findMessageResult.createdAt();
        updatedAt = findMessageResult.updatedAt();
        List<FindBinaryContentResult> list = findMessageResult.attachments();
        if ( list != null ) {
            attachments = new ArrayList<FindBinaryContentResult>( list );
        }
        content = findMessageResult.content();
        channelId = findMessageResult.channelId();
        author = findMessageResult.author();

        FindMessageResponseDTO findMessageResponseDTO = new FindMessageResponseDTO( id, createdAt, updatedAt, attachments, content, channelId, author );

        return findMessageResponseDTO;
    }

    @Override
    public FindMessageResult toFindMessageResult(Message message) {
        if ( message == null ) {
            return null;
        }

        UUID channelId = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        FindUserResult author = null;

        channelId = messageChannelId( message );
        id = message.getId();
        createdAt = message.getCreatedAt();
        updatedAt = message.getUpdatedAt();
        attachments = binaryContentListToFindBinaryContentResultList( message.getAttachments() );
        content = message.getContent();
        author = toFindUserResult( message.getAuthor() );

        FindMessageResult findMessageResult = new FindMessageResult( id, createdAt, updatedAt, attachments, content, channelId, author );

        return findMessageResult;
    }

    @Override
    public UpdateMessageResult toUpdateMessageResult(Message message) {
        if ( message == null ) {
            return null;
        }

        UUID channelId = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        FindUserResult author = null;

        channelId = messageChannelId( message );
        id = message.getId();
        createdAt = message.getCreatedAt();
        updatedAt = message.getUpdatedAt();
        attachments = binaryContentListToFindBinaryContentResultList( message.getAttachments() );
        content = message.getContent();
        author = toFindUserResult( message.getAuthor() );

        UpdateMessageResult updateMessageResult = new UpdateMessageResult( id, createdAt, updatedAt, attachments, content, channelId, author );

        return updateMessageResult;
    }

    @Override
    public CreateMessageResult toCreateMessageResult(Message message) {
        if ( message == null ) {
            return null;
        }

        UUID channelId = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        List<FindBinaryContentResult> attachments = null;
        String content = null;
        FindUserResult author = null;

        channelId = messageChannelId( message );
        id = message.getId();
        createdAt = message.getCreatedAt();
        updatedAt = message.getUpdatedAt();
        attachments = binaryContentListToFindBinaryContentResultList( message.getAttachments() );
        content = message.getContent();
        author = toFindUserResult( message.getAuthor() );

        CreateMessageResult createMessageResult = new CreateMessageResult( id, createdAt, updatedAt, attachments, content, channelId, author );

        return createMessageResult;
    }

    @Override
    public FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String filename = null;
        long size = 0L;
        String contentType = null;

        id = binaryContent.getId();
        filename = binaryContent.getFilename();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        FindBinaryContentResult findBinaryContentResult = new FindBinaryContentResult( id, filename, size, contentType );

        return findBinaryContentResult;
    }

    @Override
    public FindUserResult toFindUserResult(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        FindBinaryContentResult profile = null;
        String username = null;
        String email = null;

        id = user.getId();
        profile = toFindBinaryContentResult( user.getProfile() );
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = user.getUserStatus().isLoginUser();

        FindUserResult findUserResult = new FindUserResult( id, profile, username, email, online );

        return findUserResult;
    }

    private UUID messageChannelId(Message message) {
        if ( message == null ) {
            return null;
        }
        Channel channel = message.getChannel();
        if ( channel == null ) {
            return null;
        }
        UUID id = channel.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<FindBinaryContentResult> binaryContentListToFindBinaryContentResultList(List<BinaryContent> list) {
        if ( list == null ) {
            return null;
        }

        List<FindBinaryContentResult> list1 = new ArrayList<FindBinaryContentResult>( list.size() );
        for ( BinaryContent binaryContent : list ) {
            list1.add( toFindBinaryContentResult( binaryContent ) );
        }

        return list1;
    }
}
