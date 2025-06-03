package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  private UUID messageId;
  private UUID channelId;
  private UUID authorId;
  private String content;
  private Message message;
  private MessageDto messageDto;
  private Channel channel;
  private User author;
  private BinaryContent attachment;
  private BinaryContentDto attachmentDto;

  @BeforeEach
  void setUp() {
    messageId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();
    content = "test message";

    channel = Channel.create(ChannelType.PUBLIC, "testChannel", "testDescription");
    ReflectionTestUtils.setField(channel, "id", channelId);

    author = User.create("testUser", "test@example.com", "password", null);
    ReflectionTestUtils.setField(author, "id", authorId);

    attachment = BinaryContent.create("test.txt", 100L, "text/plain");
    ReflectionTestUtils.setField(attachment, "id", UUID.randomUUID());
    attachmentDto = new BinaryContentDto(attachment.getId(), "test.txt", 100L, "text/plain");

    message = Message.create(author, channel, content, List.of(attachment));
    ReflectionTestUtils.setField(message, "id", messageId);

    messageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        content,
        channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        List.of(attachmentDto)
    );
  }

  @Test
  @DisplayName("Create Message_성공")
  void createMessage_shouldReturnCreatedMessage() {
    // given
    MessageCreateRequest request = new MessageCreateRequest(content, authorId, channelId);
    BinaryContentCreateRequest attachmentRequest = new BinaryContentCreateRequest("test.txt",
        "text/plain", new byte[100]);

    given(userRepository.findById(authorId)).willReturn(Optional.of(author));
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(binaryContentRepository.save(any(BinaryContent.class))).will(invocation -> {
      BinaryContent binaryContent = invocation.getArgument(0);
      ReflectionTestUtils.setField(binaryContent, "id", attachment.getId());
      return attachment;
    });
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(message)).willReturn(messageDto);

    // when
    MessageDto result = messageService.createMessage(request, List.of(attachmentRequest));

    // then
    assertThat(result.content()).isEqualTo(request.content());
    assertThat(result.channelId()).isEqualTo(request.channelId());
    assertThat(result.author().id()).isEqualTo(request.authorId());

    then(userRepository).should().findById(authorId);
    then(channelRepository).should().findById(channelId);
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(attachment.getId(), attachmentRequest.bytes());
    then(messageRepository).should().save(any(Message.class));
    then(messageMapper).should().toDto(message);
  }

  @Test
  @DisplayName("Create Message_실패_존재하지 않는 작성자")
  void createMessage_shouldThrowException_whenAuthorNotFound() {
    // given
    MessageCreateRequest request = new MessageCreateRequest("메시지 테스트", authorId, channelId);
    BinaryContentCreateRequest attachmentRequest = new BinaryContentCreateRequest("test.txt",
        "text/plain", new byte[100]);

    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> messageService.createMessage(request, List.of(attachmentRequest)));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(authorId);

    then(userRepository).should().findById(authorId);
    then(channelRepository).should(never()).findById(channelId);
  }

  @Test
  @DisplayName("Update Message_성공")
  void updateMessage_shouldReturnUpdatedMessage() {
    // given
    MessageUpdateRequest request = new MessageUpdateRequest("새로운 메시지 update 테스트");

    Message updatedMessage = Message.create(author, channel, request.newContent(),
        List.of(attachment));
    ReflectionTestUtils.setField(updatedMessage, "id", messageId);

    MessageDto updatedMessageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        request.newContent(),
        channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        List.of(attachmentDto)
    );

    given(messageRepository.findById(messageId)).willReturn(Optional.of(updatedMessage));
    given(messageMapper.toDto(updatedMessage)).willReturn(updatedMessageDto);

    // when
    MessageDto result = messageService.updateMessage(messageId, request);

    // then
    assertThat(result.content()).isEqualTo(request.newContent());

    then(messageRepository).should().findById(messageId);
    then(messageMapper).should().toDto(updatedMessage);
  }

  @Test
  @DisplayName("Update Message_실패_존재하지 않는 메시지")
  void updateMessage_shouldThrowException_whenMessageNotFound() {
    // given
    MessageUpdateRequest request = new MessageUpdateRequest("새로운 메시지");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when & then
    MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
        () -> messageService.updateMessage(messageId, request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);
    assertThat(exception.getDetails().get("messageId")).isEqualTo(messageId);

    then(messageRepository).should().findById(messageId);
  }

  @Test
  @DisplayName("Delete Message_성공")
  void deleteMessage() {
    // given
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(userRepository.existsById(authorId)).willReturn(true);

    // when
    messageService.deleteMessage(messageId);

    // then
    then(messageRepository).should().findById(messageId);
    then(userRepository).should().existsById(authorId);
    then(binaryContentStorage).should().deleteById(attachment.getId());
    then(binaryContentRepository).should().deleteAll(message.getAttachments());
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("Delete Message_실패_메시지가 존재하지 않을 때")
  void deleteMessage_shouldThrowException_whenMessageNotFound() {
    // given
    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when
    MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
        () -> messageService.deleteMessage(messageId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);
    assertThat(exception.getDetails().get("messageId")).isEqualTo(messageId);

    // then
    then(messageRepository).should().findById(messageId);
    then(messageRepository).should(never()).deleteById(messageId);
    then(userRepository).shouldHaveNoMoreInteractions();
    then(binaryContentStorage).shouldHaveNoMoreInteractions();
    then(binaryContentRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("findById_성공")
  void findById_success() {
    // given
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willReturn(messageDto);

    // when
    MessageDto result = messageService.findById(messageId);

    // then
    assertThat(result).isEqualTo(messageDto);

    then(messageRepository).should().findById(messageId);
    then(messageMapper).should().toDto(message);
  }

  @Test
  @DisplayName("findById_실패_존재하지 않는 메시지 조회")
  void findById__shouldThrowException_whenNonExistentId() {
    // given
    given(messageRepository.findById(eq(messageId))).willReturn(Optional.empty());

    // when & then
    MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
        () -> messageService.findById(messageId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);
    assertThat(exception.getDetails().get("messageId")).isEqualTo(messageId);

    then(messageRepository).should().findById(messageId);
    then(messageMapper).should(never()).toDto(message);
  }

  @Test
  @DisplayName("findAllByChannelId_성공")
  void findAllByChannelId_shouldReturnMessageList() {
    // given
    int pageSize = 2; // 페이지 크기를 2로 설정
    Instant createdAt = Instant.now();
    Pageable pageable = PageRequest.of(0, pageSize);

    // 여러 메시지 생성 (페이지 사이즈보다 많게)
    Message message1 = Message.create(author, channel, content + "1", List.of(attachment));
    Message message2 = Message.create(author, channel, content + "2", List.of(attachment));
    Message message3 = Message.create(author, channel, content + "3", List.of(attachment));

    ReflectionTestUtils.setField(message1, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(message2, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(message3, "id", UUID.randomUUID());

    // 각 메시지에 해당하는 DTO 생성
    Instant message1CreatedAt = Instant.now().minusSeconds(30);
    Instant message2CreatedAt = Instant.now().minusSeconds(20);
    Instant message3CreatedAt = Instant.now().minusSeconds(10);

    ReflectionTestUtils.setField(message1, "createdAt", message1CreatedAt);
    ReflectionTestUtils.setField(message2, "createdAt", message2CreatedAt);
    ReflectionTestUtils.setField(message3, "createdAt", message3CreatedAt);

    MessageDto messageDto1 = new MessageDto(
        message1.getId(),
        message1CreatedAt,
        message1CreatedAt,
        content + "1",
        channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        List.of(attachmentDto)
    );

    MessageDto messageDto2 = new MessageDto(
        message2.getId(),
        message2CreatedAt,
        message2CreatedAt,
        content + "2",
        channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        List.of(attachmentDto)
    );

    // 첫 페이지 결과 세팅 (2개 메시지)
    List<Message> firstPageMessages = List.of(message1, message2);
    List<MessageDto> firstPageDtos = List.of(messageDto1, messageDto2);

    // 첫 페이지는 다음 페이지가 있고, 커서는 message2의 생성 시간이어야 함
    SliceImpl<Message> firstPageSlice = new SliceImpl<>(firstPageMessages, pageable, true);
    PageResponse<MessageDto> firstPageResponse = new PageResponse<>(
        firstPageDtos,
        message2CreatedAt,
        pageSize,
        true,
        null
    );

    // 모의 객체 설정
    given(messageRepository.findAllByChannelId(eq(channelId), eq(createdAt), eq(pageable)))
        .willReturn(firstPageSlice);
    given(messageMapper.toDto(eq(message1))).willReturn(messageDto1);
    given(messageMapper.toDto(eq(message2))).willReturn(messageDto2);
    given(pageResponseMapper.<MessageDto>fromSlice(any(), eq(message2CreatedAt)))
        .willReturn(firstPageResponse);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, createdAt,
        pageable);

    // then
    assertThat(result).isEqualTo(firstPageResponse);
    assertThat(result.content()).hasSize(pageSize);
    assertThat(result.hasNext()).isTrue();
    assertThat(result.nextCursor()).isEqualTo(message2CreatedAt);

    // 두 번째 페이지 테스트
    // given
    List<Message> secondPageMessages = List.of(message3);
    MessageDto messageDto3 = new MessageDto(
        message3.getId(),
        message3CreatedAt,
        message3CreatedAt,
        content + "3",
        channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        List.of(attachmentDto)
    );
    List<MessageDto> secondPageDtos = List.of(messageDto3);

    SliceImpl<Message> secondPageSlice = new SliceImpl<>(secondPageMessages, pageable, false);
    PageResponse<MessageDto> secondPageResponse = new PageResponse<>(
        secondPageDtos,
        message3CreatedAt,
        pageSize,
        false,
        null
    );

    // 두 번째 페이지 모의 객체 설정
    given(messageRepository.findAllByChannelId(eq(channelId), eq(message2CreatedAt), eq(pageable)))
        .willReturn(secondPageSlice);
    given(messageMapper.toDto(eq(message3))).willReturn(messageDto3);
    given(pageResponseMapper.<MessageDto>fromSlice(any(), eq(message3CreatedAt)))
        .willReturn(secondPageResponse);

    // when - 두 번째 페이지 요청 (첫 페이지의 커서 사용)
    PageResponse<MessageDto> secondResult = messageService.findAllByChannelId(channelId,
        message2CreatedAt,
        pageable);

    // then - 두 번째 페이지 검증
    assertThat(secondResult).isEqualTo(secondPageResponse);
    assertThat(secondResult.content()).hasSize(1); // 마지막 페이지는 항목 1개만 있음
    assertThat(secondResult.hasNext()).isFalse(); // 더 이상 다음 페이지 없음

    then(messageRepository).should().findAllByChannelId(channelId, createdAt, pageable);
  }


}
