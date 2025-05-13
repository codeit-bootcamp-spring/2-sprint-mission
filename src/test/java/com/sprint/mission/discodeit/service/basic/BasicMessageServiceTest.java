package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.controller.PageResponse;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
  private BasicBinaryContentService basicBinaryContentService;
  @Spy
  private PageResponseMapper pageResponseMapper = new PageResponseMapper() {
  };
  @InjectMocks
  private BasicMessageService basicMessageService;

  private UUID id;
  private UUID channelId;
  private UUID userId;

  private String content;
  private Channel channel;
  private User user;
  private Message message;
  private MessageDto messageDto;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
    content = "message1";

    channel = new Channel(ChannelType.PUBLIC, "channel1", "this is channel1");
    ReflectionTestUtils.setField(channel, "id", channelId);

    user = new User("user1", "user1@gmail.com", "user1234", null);
    ReflectionTestUtils.setField(user, "id", userId);

    message = new Message(content, channel, user, List.of());
    ReflectionTestUtils.setField(message, "id", id);

    messageDto = new MessageDto(
        id, Instant.now(), Instant.now(),
        content, channelId,
        new UserDto(userId, "user1", "user1@gmail.com", null, true),
        List.of()
    );
  }

  @Test
  public void createMessage_NotFound_throwChannelNotFoundException() {
    MessageCreateRequest request = new MessageCreateRequest(content, channelId, userId);
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> basicMessageService.create(request, null))
        .isInstanceOf(ChannelNotFoundException.class);
    verify(channelRepository).findById(channelId);
  }

  @Test
  public void createMessage_NotFound_throwUserNotFoundException() {
    MessageCreateRequest request = new MessageCreateRequest(content, channelId, userId);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findByIdWithProfileAndUserStatus(userId)).willReturn(
        Optional.empty());

    assertThatThrownBy(() -> basicMessageService.create(request, null))
        .isInstanceOf(UserNotFoundException.class);
    verify(channelRepository).findById(channelId);
    verify(userRepository).findByIdWithProfileAndUserStatus(userId);
  }

  @Test
  public void createMessage_success() { // 테스트가 안 끝나는 문제가 있음.
    MessageCreateRequest request = new MessageCreateRequest(content, channelId, userId);
    List<BinaryContentCreateRequest> emptyBinaries = List.of();

    given(channelRepository.findById(eq(channelId)))
        .willReturn(Optional.of(channel));
    given(userRepository.findByIdWithProfileAndUserStatus(eq(userId)))
        .willReturn(Optional.of(user));

    given(messageRepository.save(any(Message.class)))
        .willReturn(message);

    given(messageMapper.toDto(any(Message.class)))
        .willReturn(messageDto);

    MessageDto res = basicMessageService.create(request, emptyBinaries);

    System.out.println("▶ after create(), res: " + res);
    System.out.println("▶ after create(), messageDto: " + messageDto);
    then(res).isEqualTo(messageDto);
    verify(channelRepository).findById(channelId);
    verify(userRepository).findByIdWithProfileAndUserStatus(userId);
    verify(messageRepository).save(any(Message.class));
  }

  @Test
  public void updateMessage_NotFound_throwMessageNotFoundException() {
    MessageUpdateRequest request = new MessageUpdateRequest("newMessage");
    given(messageRepository.findByIdWithAuthorAndAttachments(id)).willReturn(
        Optional.empty());

    assertThatThrownBy(() -> basicMessageService.update(id, request))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  public void updateMessage_success() {
    String newContent = "newContent";
    MessageUpdateRequest request = new MessageUpdateRequest(newContent);
    given(messageRepository.findByIdWithAuthorAndAttachments(id)).willReturn(Optional.of(message));
    message.update(newContent);

    MessageDto dto = new MessageDto(id, Instant.now(), Instant.now(), newContent, channelId,
        new UserDto(userId, "user1", "user1@gmail.com", null, true),
        List.of());
    given(messageMapper.toDto(any(Message.class))).willReturn(dto);

    MessageDto res = basicMessageService.update(id, request);
    then(res).isEqualTo(dto);
    verify(messageRepository).findByIdWithAuthorAndAttachments(id);
  }

  @Test
  public void deleteMessage_NotFound_throwMessageNotFoundException() {
    given(messageRepository.existsById(id)).willReturn(false);

    assertThatThrownBy(() -> basicMessageService.delete(id))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  public void deleteMessage_success() {
    given(messageRepository.existsById(id)).willReturn(true);

    basicMessageService.delete(id);

    verify(messageRepository).deleteById(id);
  }

  @Test
  public void findAllByChannelId_success() {
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.parse("2025-05-13T10:00:00Z");
    Pageable pageable = PageRequest.of(0, 2);

    Message m1 = new Message("m1", null, null, List.of());
    ReflectionTestUtils.setField(m1, "id", UUID.randomUUID());
    Message m2 = new Message("m2", null, null, List.of());
    ReflectionTestUtils.setField(m2, "id", UUID.randomUUID());
    Slice<Message> slice = new SliceImpl<>(List.of(m1, m2), pageable, true);

    given(messageRepository
        .findAllByChannelIdWithAuthor(channelId, cursor, pageable))
        .willReturn(slice);

    Instant ts1 = Instant.parse("2025-05-13T09:00:00Z");
    Instant ts2 = Instant.parse("2025-05-13T09:30:00Z");
    MessageDto d1 = new MessageDto(m1.getId(), ts1, ts1, "m1", channelId, null, List.of());
    MessageDto d2 = new MessageDto(m2.getId(), ts2, ts2, "m2", channelId, null, List.of());

    given(messageMapper.toDto(m1)).willReturn(d1);
    given(messageMapper.toDto(m2)).willReturn(d2);

    PageResponse<MessageDto> result =
        basicMessageService.findAllByChannelId(channelId, cursor, pageable);

    then(result.content()).containsExactly(d1, d2);
    then((Instant) result.nextCursor()).isEqualTo(ts2);

    verify(messageRepository)
        .findAllByChannelIdWithAuthor(channelId, cursor, pageable);
  }
}
