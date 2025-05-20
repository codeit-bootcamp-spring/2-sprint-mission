package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
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
    @Mock
    private Pageable pageable;

    @InjectMocks
    private BasicMessageService messageService;

    @Test
    @DisplayName("정상적인 Message 생성 테스트")
    void createMessage_WithValidInput_Success() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사", "인사 채널");

        forceSetId(user, authorId);
        forceSetId(channel, channelId);

        MessageCreateRequest request = new MessageCreateRequest("안녕하세요", channelId, authorId);

        Message message = new Message("안녕하세요", channel, user, List.of());
        forceSetId(message, messageId);
        forceSetCreatedAt(message, createdAt);

        given(userRepository.findById(eq(authorId))).willReturn(Optional.of(user));
        given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
        given(messageRepository.save(any())).willReturn(message);

        MessageDto expectedDto = new MessageDto(
                messageId,
                createdAt,
                null,
                "안녕하세요",
                channelId,
                null,
                List.of()
        );
        given(messageMapper.toDto(any())).willReturn(expectedDto);

        MessageDto messageDto = messageService.create(request, List.of());

        assertEquals("안녕하세요", messageDto.content());
        assertEquals(channelId, messageDto.channelId());
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
        UUID messageId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        String content = "안녕하세요";

        MessageCreateRequest request = new MessageCreateRequest(content, channelId, authorId);

        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사", "인사 채널");
        forceSetId(user, authorId);
        forceSetId(channel, channelId);

        byte[] fileBytes = "fileContent".getBytes();
        BinaryContentCreateRequest attachment = new BinaryContentCreateRequest("file.png", "image/png", fileBytes);

        BinaryContent binaryContent = new BinaryContent("file.png", (long) fileBytes.length, "image/png");

        Message message = new Message(content, channel, user, List.of(binaryContent));
        forceSetId(message, messageId);
        forceSetCreatedAt(message, createdAt);

        MessageDto expectedDto = new MessageDto(
                messageId,
                createdAt,
                null,
                content,
                channelId,
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
        assertEquals(channelId, messageDto.channelId());

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

        MessageCreateRequest request = new MessageCreateRequest("message", channelId, noExistId);

        given(userRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> messageService.create(request, List.of()));

        verify(userRepository).findById(noExistId);
    }

    @Test
    @DisplayName("존재하지 않는 channelId로 메세지 생성 시 예외 발생 테스트")
    void createMessage_WithInvalidChannelId_ThrowException() {
        UUID authorId = UUID.randomUUID();
        UUID noExistId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("message", noExistId, authorId);

        User user = new User("user1", "user1@gmail.com", "1234", null);
        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(ChannelNotFoundException.class, () -> messageService.create(request, List.of()));

        verify(channelRepository).findById(noExistId);
    }

    @Test
    @DisplayName("정상적인 메세지 update 테스트")
    void updateMessage_WithValidInput_Success() {
        UUID messageId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "인사 채널", "인사하는 채널");
        Message message = new Message("안녕하세요", channel, user, List.of());
        forceSetId(message, messageId);
        forceSetCreatedAt(message, createdAt);

        String newContent = "안녕하세요, 반갑습니다.";
        MessageUpdateRequest request = new MessageUpdateRequest(newContent);

        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        MessageDto expectedDto = new MessageDto(
                messageId,
                createdAt,
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

    @Test
    @DisplayName("정상적인 Message delete 테스트")
    void deleteMessage_WithValidInput_Success() {
        UUID messageId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        User user = new User("user1", "user1@gmail.com", "1234", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지 채널");
        Message message = new Message("삭제할 메시지", channel, user, List.of());
        forceSetId(message, messageId);
        forceSetCreatedAt(message, createdAt);

        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        messageService.deleteMessage(message.getId());

        verify(messageRepository).findById(message.getId());
        verify(messageRepository).delete(message);
    }

    @Test
    @DisplayName("존재하지 않는 messageId로 삭제 시 예외 발생 테스트")
    void deleteMessage_WithInvalidMessageId_ThrowException() {
        UUID noExistId = UUID.randomUUID();

        given(messageRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.deleteMessage(noExistId));

        verify(messageRepository).findById(noExistId);
    }

    @Test
    @DisplayName("채널 ID로 메세지 목록 조회 성공 테스트 - 메세지가 존재하는 경우")
    void findAllByChannelId_WithMessages_Success() {
        UUID channelId = UUID.randomUUID();

        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지 채널");
        forceSetId(channel, channelId);

        Message msg1 = new Message("안녕하세요", channel, null, List.of());
        Message msg2 = new Message("감사합니다", channel, null, List.of());

        Instant createdAt1 = Instant.now();
        Instant createdAt2 = createdAt1.plusSeconds(10);
        forceSetId(msg1, UUID.randomUUID());
        forceSetId(msg2, UUID.randomUUID());
        forceSetCreatedAt(msg1, createdAt1);
        forceSetCreatedAt(msg2, createdAt2);

        MessageDto dto1 = new MessageDto(
                msg1.getId(), createdAt1, null, msg1.getContent(), channelId, null, List.of());
        MessageDto dto2 = new MessageDto(
                msg2.getId(), createdAt2, null, msg2.getContent(), channelId, null, List.of());

        Slice<MessageDto> dtoSlice = new SliceImpl<>(List.of(dto1, dto2), Pageable.ofSize(2), true);

        given(messageRepository.findAllByChannelIdWithAuthor(any(), any(), any()))
                .willReturn(mock(Slice.class, invocation -> dtoSlice));

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

        assertEquals(2, result.content().size());
        assertTrue(result.hasNext());
        assertEquals(createdAt2, result.nextCursor());
    }

    @Test
    @DisplayName("채널 ID로 메세지 목록 조회 테스트 - 메세지가 없는 경우")
    void findAllByChannelId_EmptyResult() {
        UUID channelId = UUID.randomUUID();

        Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);

        given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
                .willReturn(emptySlice);

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

        assertEquals(0, result.content().size());
        assertFalse(result.hasNext());
        assertNull(result.nextCursor());
    }

    private void forceSetId(Object entity, UUID id) {
        Field idField = ReflectionUtils.findField(BaseEntity.class, "id");
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, entity, id);
    }

    private void forceSetCreatedAt(Object entity, Instant createdAt) {
        Field createdAtField = ReflectionUtils.findField(BaseEntity.class, "createdAt");
        ReflectionUtils.makeAccessible(createdAtField);
        ReflectionUtils.setField(createdAtField, entity, createdAt);

    }

}
