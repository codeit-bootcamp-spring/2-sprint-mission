package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.lenient;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

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
  private BasicMessageService messageService;

  private UUID channelId;
  private UUID authorId;
  private UUID messageId;
  private Channel channel;
  private User author;
  private Message message;
  private MessageDto messageDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();
    messageId = UUID.randomUUID();
    channel = new Channel(ChannelType.PUBLIC, "testChannel", "text");
    author = new User("testUser", "user@example.com", "password", null);
    message = new Message("original content", channel, author, List.of());
    messageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "test content",
        channelId,
        null,
        List.of()
    );
  }

  @Nested
  @DisplayName("create() 테스트")
  class CreateTests {

    @Test
    @DisplayName("성공: 정상 요청시 MessageDto 반환")
    void createSuccess() {
      // Given
      MessageCreateRequest request = mock(MessageCreateRequest.class);
      given(request.channelId()).willReturn(channelId);
      given(request.authorId()).willReturn(authorId);
      given(request.content()).willReturn("Hello");
      given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
      given(userRepository.findById(authorId)).willReturn(Optional.of(author));
      given(messageMapper.toDto(any())).willReturn(messageDto);

      // When
      MessageDto result = messageService.create(request, List.of());

      // Then
      assertThat(result).isEqualTo(messageDto);
      then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("실패: 존재하지 않는 채널 ID")
    void createChannelNotFound() {
      // Given
      MessageCreateRequest request = mock(MessageCreateRequest.class);
      given(request.channelId()).willReturn(UUID.randomUUID());
      given(channelRepository.findById(any())).willReturn(Optional.empty());

      // When & Then
      assertThatThrownBy(() -> messageService.create(request, List.of()))
          .isInstanceOf(ChannelNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("update() 테스트")
  class UpdateTests {

    @Test
    @DisplayName("성공: 컨텐츠 업데이트")
    void updateSuccess() {
      // Given
      MessageUpdateRequest request = new MessageUpdateRequest("new content");
      given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
      given(messageMapper.toDto(any())).willReturn(messageDto);

      // When
      MessageDto result = messageService.update(messageId, request);

      // Then
      assertThat(result.content()).isEqualTo("test content");
      assertThat(message.getContent()).isEqualTo("new content");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 메시지 ID")
    void updateMessageNotFound() {
      // Given
      given(messageRepository.findById(any())).willReturn(Optional.empty());

      // When & Then
      assertThatThrownBy(
          () -> messageService.update(UUID.randomUUID(), new MessageUpdateRequest("new")))
          .isInstanceOf(MessageNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("delete() 테스트")
  class DeleteTests {

    @Test
    @DisplayName("성공: 메시지 삭제")
    void deleteSuccess() {
      // Given
      given(messageRepository.existsById(messageId)).willReturn(true);

      // When
      messageService.delete(messageId);

      // Then
      then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 메시지 삭제 시도")
    void deleteMessageNotFound() {
      // Given
      given(messageRepository.existsById(any())).willReturn(false);

      // When & Then
      assertThatThrownBy(() -> messageService.delete(UUID.randomUUID()))
          .isInstanceOf(MessageNotFoundException.class);
    }
  }

  @Test
  @DisplayName("성공: 채널별 메시지 목록 조회")
  void findAllSuccess() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Message message = new Message("test", channel, author, List.of());
    Slice<Message> slice = new SliceImpl<>(List.of(message), pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class),
        eq(pageable)))
        .willReturn(slice);
    given(messageMapper.toDto(message)).willReturn(messageDto);

    given(pageResponseMapper.fromSlice(any(), any()))
        .willReturn(new PageResponse<>(
            List.of(messageDto),
            Instant.now(),
            10,
            false,
            1L
        ));

    // When
    PageResponse<MessageDto> result =
        messageService.findAllByChannelId(channelId, null, pageable);

    // Then
    assertThat(result.content()).hasSize(1);
  }

  @Test
  @DisplayName("성공: 빈 결과 조회")
  void findAllEmpty() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(any(), any(), any()))
        .willReturn(emptySlice);

    lenient().when(pageResponseMapper.fromSlice(
        any(Slice.class),
        isNull() // nextCursor가 null인 경우
    )).thenReturn(new PageResponse<>(
        List.of(),
        null,
        0,
        false,
        0L
    ));

    // When
    PageResponse<MessageDto> result =
        messageService.findAllByChannelId(UUID.randomUUID(), null, pageable);

    // Then
    assertThat(result.content()).isEmpty();
  }
}


