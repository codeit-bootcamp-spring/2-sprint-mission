package com.sprint.mission.discodeit.unit;

import com.sprint.mission.discodeit.Mapper.MessageMapper;
import com.sprint.mission.discodeit.Mapper.PageResponseMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFound;
import com.sprint.mission.discodeit.exception.message.MessageNotFound;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataMessageAttachmentRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    MessageRepository messageRepository;
    @Mock
    ChannelRepository channelRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    SpringDataMessageAttachmentRepository messageAttachmentRepository;
    @Mock
    MessageMapper messageMapper;
    @Mock
    PageResponseMapper pageResponseMapper;
    @Mock
    BinaryContentStorage binaryContentStorage;

    @InjectMocks
    BasicMessageService messageService;

    @Test
    @DisplayName("메시지 생성 성공")
    void createSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId =UUID.randomUUID();
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest("메시지 내용", channelId, authorId);
        Channel mockChannel = new Channel(ChannelType.PUBLIC, "name", "desc");
        User    mockUser    = new User("u","u@mail.com","pw", null);

        given(channelRepository.existsById(channelId)).willReturn(true);
        given(userRepository.existsById(authorId)).willReturn(true);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(mockUser));

        Message message = new Message("메시지 내용", mockChannel, mockUser);
        given(messageRepository.save(any(Message.class))).willReturn(message);

        MessageDto messageDto = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "메시지내용", channelId, new UserDto(authorId, "username", "userEmail", null, true), Collections.emptyList());
        given(messageMapper.toDto(message)).willReturn(messageDto);

        // when
        MessageDto result = messageService.create(messageCreateRequest, Collections.emptyList());

        // then
        then(channelRepository).should().existsById(channelId);
        then(userRepository).should().existsById(authorId);
        then(channelRepository).should().findById(channelId);
        then(messageRepository).should().save(any(Message.class));
        then(messageMapper).should().toDto(message);
        assertThat(result).isEqualTo(messageDto);
    }

    @Test
    @DisplayName("메시지 생성 실패")
    void createFail() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId =UUID.randomUUID();
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest("메시지 내용", channelId, authorId);
        given(channelRepository.existsById(channelId)).willReturn(false);

        // when
        Throwable thrown = catchThrowable( () ->  messageService.create(messageCreateRequest, Collections.emptyList()));

        // then
        assertThat(thrown).isInstanceOf(ChannelNotFound.class);

    }

    @Test
    @DisplayName("메시지 업데이트 성공")
    void updateSuccess() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new_content");

        // 1) findById → existingMessage 리턴
        Channel mockChannel = new Channel(ChannelType.PUBLIC, "name", "desc");
        User mockUser = new User("u", "u@mail.com", "pw", null);
        Message existingMessage = new Message("old_content", mockChannel, mockUser);
        given(messageRepository.findById(messageId))
                .willReturn(Optional.of(existingMessage));

        // 2) save(existingMessage) → updatedMessage 리턴
        Message updatedMessage = new Message("메시지 내용", mockChannel, mockUser);
        given(messageRepository.save(existingMessage))
                .willReturn(updatedMessage);

        // 3) mapper.toDto(updatedMessage) → expectedDto 리턴
        MessageDto expectedDto = new MessageDto(
                messageId,
                updatedMessage.getCreatedAt(),
                updatedMessage.getUpdatedAt(),
                "메시지내용",
                mockChannel.getId(),
                new UserDto(mockUser.getId(), mockUser.getUsername(), mockUser.getEmail(), null, true),
                Collections.emptyList()
        );
        given(messageMapper.toDto(updatedMessage))
                .willReturn(expectedDto);

        // when
        MessageDto result = messageService.update(messageId, messageUpdateRequest);

        // then
        then(messageRepository).should().findById(messageId);
        then(messageRepository).should().save(existingMessage);
        then(messageMapper).should().toDto(updatedMessage);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("메시지 업데이트 실패")
    void updateFailed() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new_content");

        Channel mockChannel = new Channel(ChannelType.PUBLIC, "name", "desc");
        User mockUser = new User("u", "u@mail.com", "pw", null);
        Message existingMessage = new Message("old_content", mockChannel, mockUser);
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable( () -> messageService.update(messageId, messageUpdateRequest));

        // then
        assertThat(thrown).isInstanceOf(MessageNotFound.class);

    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteSuccess() {
        // given
        UUID messageId = UUID.randomUUID();

        // when
        assertDoesNotThrow( () -> messageService.delete(messageId));

        // then
        then(messageRepository).should().deleteById(messageId);
    }


}
