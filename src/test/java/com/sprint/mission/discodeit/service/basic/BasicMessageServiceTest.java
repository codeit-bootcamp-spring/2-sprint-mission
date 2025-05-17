package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.*;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService basicMessageService;

  private UUID messageId;
  private UUID channelId;
  private UUID userId;
  private Channel channel;
  private User user;
  private Message message;
  private MessageDto messageDto;

  @BeforeEach
  void setUp() {
    messageId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();

    user = new User("testUser", "test@example.com", "password", null);
    channel = new Channel(ChannelType.PUBLIC, "testChannel", "desc");
    message = new Message("Hello", channel, user, List.of());

    Instant now = Instant.now();
    BinaryContentDto profile = new BinaryContentDto(UUID.randomUUID(), "profile.png", 2048L,
        "image/png");
    UserDto authorDto = new UserDto(userId, "testUser", "test@example.com", profile, true);

    List<BinaryContentDto> attachments = List.of();

    messageDto = new MessageDto(
        messageId,
        now,
        now,
        "Hello",
        channelId,
        authorDto,
        attachments
    );
  }

  @Test
  @DisplayName("메시지 생성 성공")
  void testCreateMessageSuccess() {
    UUID attachmentId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, userId);
    BinaryContentCreateRequest binaryRequest = new BinaryContentCreateRequest("file.txt",
        "text/plain", "data".getBytes());
    List<BinaryContentCreateRequest> binaryRequests = List.of(binaryRequest);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);
    given(binaryContentRepository.save(any(BinaryContent.class))).will(invocation -> {
      BinaryContent arg = invocation.getArgument(0);
      ReflectionTestUtils.setField(arg, "id", attachmentId);
      return arg;
    });

    MessageDto result = basicMessageService.create(request, binaryRequests);

    assertEquals(messageDto, result);

    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(userId);
    then(messageRepository).should().save(any(Message.class));
    then(messageMapper).should().toDto(any(Message.class));
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(any(UUID.class), any());
  }

  @Test
  @DisplayName("메시지 생성 실패 - 채널 없음")
  void testCreateMessageFailure_channelNotFound() {
    MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, userId);
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    ChannelException exception = assertThrows(ChannelException.class, () -> {
      basicMessageService.create(request, List.of());
    });

    assertEquals(ErrorCode.CHANNEL_NOT_FOUND.getMessage(), exception.getMessage());

    then(channelRepository).should().findById(channelId);
    then(channelRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("메시지 생성 실패 - 사용자 없음")
  void testCreateMessageFailure_userNotFound() {
    MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, userId);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    UserException exception = assertThrows(UserException.class, () -> {
      basicMessageService.create(request, List.of());
    });

    assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());

    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(userId);
    then(channelRepository).shouldHaveNoMoreInteractions();
    then(userRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("메시지 업데이트 성공")
  void testUpdateMessageSuccess() {
    MessageUpdateRequest request = new MessageUpdateRequest("Updated");

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willReturn(messageDto);

    MessageDto result = basicMessageService.update(messageId, request);

    assertEquals(messageDto, result);

    then(messageRepository).should().findById(messageId);
    then(messageMapper).should().toDto(message);
  }

  @Test
  @DisplayName("메시지 업데이트 실패 - 존재하지 않음")
  void testUpdateMessageFailure_notFound() {
    MessageUpdateRequest request = new MessageUpdateRequest("Updated");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    MessageException exception = assertThrows(MessageException.class, () -> {
      basicMessageService.update(messageId, request);
    });

    assertEquals(ErrorCode.MESSAGE_NOT_FOUND.getMessage(), exception.getMessage());

    then(messageRepository).should().findById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void testDeleteMessageSuccess() {
    given(messageRepository.existsById(messageId)).willReturn(true);

    basicMessageService.delete(messageId);

    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패 - 존재하지 않음")
  void testDeleteMessageFailure_notFound() {
    given(messageRepository.existsById(messageId)).willReturn(false);

    MessageException exception = assertThrows(MessageException.class, () -> {
      basicMessageService.delete(messageId);
    });

    assertEquals(ErrorCode.MESSAGE_NOT_FOUND.getMessage(), exception.getMessage());

    then(messageRepository).should().existsById(messageId);
  }

  @Test
  @DisplayName("채널 내 메시지 목록 조회 성공")
  void testFindAllByChannelIdSuccess() {
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);

    Slice<Message> messageSlice = new SliceImpl<>(List.of(message), pageable, false);
    List<MessageDto> dtoList = List.of(messageDto);
    PageResponse<MessageDto> expectedPageResponse =
        new PageResponse<>(dtoList, null, dtoList.size(), false, null);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
        .willReturn(messageSlice);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);
    given(pageResponseMapper.fromSlice(any(Slice.class), any()))
        .willReturn(expectedPageResponse);

    PageResponse<MessageDto> result =
        basicMessageService.findAllByChannelId(channelId, now, pageable);

    assertEquals(expectedPageResponse, result);

    then(messageRepository).should()
        .findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable));
    then(messageMapper).should().toDto(any(Message.class));
    then(pageResponseMapper).should().fromSlice(any(Slice.class), any());
  }

  @Test
  @DisplayName("채널 내 메시지 목록 조회 - 빈 결과")
  void testFindAllByChannelIdEmptyResult() {
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);

    Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);
    PageResponse<MessageDto> expectedPageResponse =
        new PageResponse<>(List.of(), null, 0, false, null);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
        .willReturn(emptySlice);
    given(pageResponseMapper.fromSlice(eq(emptySlice), eq(null)))
        .willReturn((PageResponse) expectedPageResponse);

    PageResponse<MessageDto> result =
        basicMessageService.findAllByChannelId(channelId, now, pageable);

    assertEquals(expectedPageResponse, result);
    assertTrue(result.content().isEmpty());

    then(messageRepository).should()
        .findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable));
    then(pageResponseMapper).should().fromSlice(eq(emptySlice), eq(null));
  }
}
