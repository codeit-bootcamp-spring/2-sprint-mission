package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @Test
  @DisplayName("Create Message_성공")
  void createMessage_shouldReturnCreatedMessage() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("메시지 테스트", authorId, channelId);

    User author = User.create("유저", "test@email.com", "password123!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel", "테스트용 Public 채널입니다.");
    ReflectionTestUtils.setField(author, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);

    UserDto authorDto = new UserDto(authorId, author.getUsername(), author.getEmail(), null, true);

    BinaryContentCreateRequest binaryRequest = new BinaryContentCreateRequest("file.png",
        "image/png",
        new byte[]{1, 2, 3});
    BinaryContent savedBinary = BinaryContent.create(binaryRequest.fileName(), 3L,
        binaryRequest.contentType());

    Message savedMessage = Message.create(author, channel, request.content(), null);
    ReflectionTestUtils.setField(savedMessage, "id", messageId);

    MessageDto messageDto = new MessageDto(
        messageId,
        savedMessage.getCreatedAt(),
        savedMessage.getUpdatedAt(),
        savedMessage.getContent(),
        channelId,
        authorDto,
        null
    );

    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(savedBinary);
    when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
    when(messageMapper.toDto(savedMessage)).thenReturn(messageDto);

    // when
    MessageDto result = messageService.createMessage(request, List.of(binaryRequest));

    // then
    assertThat(result.content()).isEqualTo(request.content());
    assertThat(result.channelId()).isEqualTo(request.channelId());
    assertThat(result.author().id()).isEqualTo(request.authorId());

    verify(userRepository).findById(authorId);
    verify(channelRepository).findById(channelId);
    verify(binaryContentRepository).save(any(BinaryContent.class));
    verify(binaryContentStorage).put(savedBinary.getId(), binaryRequest.bytes());
    verify(messageRepository).save(any(Message.class));
    verify(messageMapper).toDto(savedMessage);
  }

  @Test
  @DisplayName("Create Message_실패_존재하지 않는 작성자")
  void createMessage_shouldThrowException_whenAuthorNotFound() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("메시지 테스트", authorId, channelId);
    BinaryContentCreateRequest binaryRequest = new BinaryContentCreateRequest("file.png",
        "image/png",
        new byte[]{1, 2, 3});

    when(userRepository.findById(authorId)).thenReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> messageService.createMessage(request, List.of(binaryRequest)));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(authorId);

    verify(userRepository).findById(authorId);
    verify(channelRepository, never()).findById(channelId);
  }

  @Test
  @DisplayName("Update Message_성공")
  void updateMessage_shouldReturnUpdatedMessage() {
    // given
    UUID messageId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageUpdateRequest request = new MessageUpdateRequest("새로운 메시지 update 테스트");

    User author = User.create("유저", "test@email.com", "password123!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel", "테스트용 Public 채널입니다.");
    ReflectionTestUtils.setField(author, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);

    UserDto authorDto = new UserDto(authorId, author.getUsername(), author.getEmail(), null, true);

    Message message = Message.create(author, channel, "테스트 메시지", null);
    ReflectionTestUtils.setField(message, "id", messageId);

    MessageDto messageDto = new MessageDto(
        messageId,
        message.getCreatedAt(),
        Instant.now(),
        request.newContent(),
        channelId,
        authorDto,
        null
    );

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toDto(message)).thenReturn(messageDto);

    // when
    MessageDto result = messageService.updateMessage(messageId, request);

    // then
    assertThat(result.content()).isEqualTo(request.newContent());

    verify(messageRepository).findById(messageId);
    verify(messageMapper).toDto(message);
  }

  @Test
  @DisplayName("Update Message_실패_존재하지 않는 메시지")
  void updateMessage_shouldThrowException_whenMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("새로운 메시지");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when & then
    MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
        () -> messageService.updateMessage(messageId, request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);
    assertThat(exception.getDetails().get("messageId")).isEqualTo(messageId);

    verify(messageRepository).findById(messageId);
  }

  @Test
  @DisplayName("Delete Message_성공")
  void deleteMessage() {
    // given
    UUID messageId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID attachmentId1 = UUID.randomUUID();
    UUID attachmentId2 = UUID.randomUUID();

    User author = User.create("유저", "test@email.com", "password123!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel", "테스트용 Public 채널입니다.");
    ReflectionTestUtils.setField(author, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);

    BinaryContent attachment1 = BinaryContent.create("image1.png", 3L, "image/png");
    BinaryContent attachment2 = BinaryContent.create("image2.png", 3L, "image/png");
    ReflectionTestUtils.setField(attachment1, "id", attachmentId1);
    ReflectionTestUtils.setField(attachment2, "id", attachmentId2);

    Message message = Message.create(author, channel, "테스트 메시지", List.of(attachment1, attachment2));
    ReflectionTestUtils.setField(message, "id", messageId);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));

    // when
    messageService.deleteMessage(messageId);

    // then
    verify(messageRepository).findById(messageId);
    verify(userRepository).findById(authorId);
    verify(binaryContentStorage).deleteById(attachment1.getId());
    verify(binaryContentStorage).deleteById(attachment2.getId());
    verify(binaryContentRepository).deleteAll(message.getAttachments());
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("Delete Message_실패_메시지가 존재하지 않을 때")
  void deleteMessage_shouldThrowException_whenMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when
    MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
        () -> messageService.deleteMessage(messageId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);
    assertThat(exception.getDetails().get("messageId")).isEqualTo(messageId);

    // then
    verify(messageRepository).findById(messageId);
    verify(messageRepository, never()).deleteById(messageId);
    verifyNoMoreInteractions(userRepository, binaryContentStorage, binaryContentRepository);
  }

  @Test
  @DisplayName("findAllByChannelId_성공")
  void findAllByChannelId_shouldReturnMessageList() {
    // given
    UUID messageId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    Instant createdAt = Instant.parse("2024-05-01T12:00:00Z");

    User author = User.create("유저", "test@email.com", "password123!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel", "테스트용 Public 채널입니다.");
    ReflectionTestUtils.setField(author, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);
    UserDto authorDto = new UserDto(authorId, author.getUsername(), author.getEmail(), null, true);

    Message message1 = Message.create(author, channel, "테스트 메시지",
        List.of());
    Instant messageCreatedAt = Instant.parse("2024-04-30T10:00:00Z");
    ReflectionTestUtils.setField(message1, "createdAt", messageCreatedAt);

    MessageDto messageDto1 = new MessageDto(
        messageId,
        message1.getCreatedAt(),
        Instant.now(),
        message1.getContent(),
        channelId,
        authorDto,
        List.of()
    );

    List<Message> messages = List.of(message1);
    Slice<Message> slice = new SliceImpl<>(messages);

    Pageable pageable = PageRequest.of(0, 10);

    PageResponse<MessageDto> expectedResponse = new PageResponse<>(
        List.of(messageDto1),
        messageDto1.createdAt(),
        10,
        false,
        null
    );

    // when
    when(messageRepository.findAllByChannelId(channelId, createdAt, pageable))
        .thenReturn(slice);
    when(messageMapper.toDto(message1)).thenReturn(messageDto1);
    when(pageResponseMapper.fromSlice(any(Slice.class), eq(messageCreatedAt)))
        .thenReturn(expectedResponse);

    // then
    PageResponse<MessageDto> actualResponse =
        messageService.findAllByChannelId(channelId, createdAt, pageable);

    assertThat(actualResponse).isEqualTo(expectedResponse);
    verify(messageRepository).findAllByChannelId(channelId, createdAt, pageable);
    verify(messageMapper).toDto(message1);
    verify(pageResponseMapper).fromSlice(any(Slice.class), eq(messageCreatedAt));
  }

  @Test
  @DisplayName("findAllByChannelId_결과 없는 경우 빈 리스트 반환")
  void findAllByChannelId_shouldReturnEmptyList() {
    // given
    UUID channelId = UUID.randomUUID();
    Instant createdAt = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);

    Slice<Message> emptySlice = new SliceImpl<>(List.of());
    PageResponse<MessageDto> expectedResponse = new PageResponse<>(
        List.of(), null, 10, false, null
    );

    when(messageRepository.findAllByChannelId(channelId, createdAt, pageable))
        .thenReturn(emptySlice);
    when(pageResponseMapper.fromSlice(any(Slice.class), isNull()))
        .thenReturn(expectedResponse);

    // when
    PageResponse<MessageDto> response =
        messageService.findAllByChannelId(channelId, createdAt, pageable);

    // then
    assertThat(response).isEqualTo(expectedResponse);
    verify(messageRepository).findAllByChannelId(channelId, createdAt, pageable);
    verify(pageResponseMapper).fromSlice(emptySlice, null);
  }

}
