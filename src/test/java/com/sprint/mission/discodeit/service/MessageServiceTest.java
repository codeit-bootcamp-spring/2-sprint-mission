package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @Test
    @DisplayName("정상적인 Message 생성 테스트")
    void createMessage_WithValidInput_Success() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("안녕하세요", authorId, channelId);

        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사", "인사 채널");
        Message message = new Message("안녕하세요", channel, user, List.of());

        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(messageRepository.save(any())).willReturn(message);

        MessageDto expectedDto = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                null,
                "안녕하세요",
                channel.getId(),
                null,
                List.of()
        );
        given(messageMapper.toDto(any())).willReturn(expectedDto);

        MessageDto messageDto = messageService.create(request, List.of());

        assertEquals("안녕하세요", messageDto.content());
        assertEquals(channel.getId(), messageDto.channelId());
        assertEquals(List.of(), messageDto.attachments());

        verify(userRepository).findById(authorId);
        verify(channelRepository).findById(channelId);
        verify(messageRepository).save(any());
        verify(messageMapper).toDto(any());
    }

    @Test
    @DisplayName("첨부파일을 포함한 정상적인 Message 생성 테스트")
    void createMessage_WithAttachments_Success() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        String content = "안녕하세요";

        MessageCreateRequest request = new MessageCreateRequest(content, authorId, channelId);

        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사", "인사 채널");

        byte[] fileBytes = "fileContent".getBytes();
        BinaryContentCreateRequest attachment = new BinaryContentCreateRequest("file.png", "image/png", fileBytes);

        BinaryContent binaryContent = new BinaryContent("file.png", (long) fileBytes.length, "image/png");

        Message message = new Message(content, channel, user, List.of(binaryContent));
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(message, "createdAt", Instant.now());

        MessageDto expectedDto = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                null,
                content,
                channel.getId(),
                null,
                List.of()
        );
        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(binaryContentRepository.save(any())).willReturn(binaryContent);
        given(messageRepository.save(any())).willReturn(message);
        given(messageMapper.toDto(any())).willReturn(expectedDto);

        MessageDto messageDto = messageService.create(request, List.of(attachment));

        assertEquals(content, messageDto.content());
        assertEquals(channel.getId(), messageDto.channelId());

        verify(binaryContentRepository).save(any());
        verify(binaryContentStorage).put(any(), eq(fileBytes));
        verify(messageRepository).save(any());
        verify(messageMapper).toDto(any());
    }

    @Test
    @DisplayName("존재하지 않는 userId로 메세지 생성 시 예외 발생 테스트")
    void createMessage_WithInvalidUserId_ThrowException() {
        UUID noExistId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("message", noExistId, channelId);

        given(userRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> messageService.create(request, List.of()));

        verify(userRepository).findById(noExistId);
    }

    @Test
    @DisplayName("존재하지 않는 channelId로 메세지 생성 시 예외 발생 테스트")
    void createMessage_WithInvalidChannelId_ThrowException() {
        UUID authorId = UUID.randomUUID();
        UUID noExistId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("message", authorId, noExistId);

        User user = new User("user1", "user1@gmail.com", "1234", null);
        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(ChannelNotFoundException.class, () -> messageService.create(request, List.of()));

        verify(channelRepository).findById(noExistId);
    }

    @Test
    @DisplayName("정상적인 메세지 update 테스트")
    void updateMessage_WithValidInput_Success() {
        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사 채널", "인사하는 채널");
        Message message = new Message("안녕하세요", channel, user, List.of());
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());

        String newContent = "안녕하세요, 반갑습니다.";
        MessageUpdateRequest request = new MessageUpdateRequest(newContent);

        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        MessageDto expectedDto = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                null,
                newContent,
                channel.getId(),
                null,
                List.of()
        );
        given(messageMapper.toDto(any())).willReturn(expectedDto);

        MessageDto messageDto = messageService.updateMessage(message.getId(), request);

        assertEquals(newContent, messageDto.content());
        verify(messageRepository).findById(message.getId());
        verify(messageMapper).toDto(message);
    }

    @Test
    @DisplayName("존재하지 않는 messageId로 업데이트 시 예외 발생 테스트")
    void updateMessage_WithInvalidMessageId_ThrowException() {
        UUID noExistId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

        given(messageRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () ->
                messageService.updateMessage(noExistId, request));

        verify(messageRepository).findById(noExistId);
    }
}
