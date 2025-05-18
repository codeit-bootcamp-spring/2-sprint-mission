package com.sprint.mission.discodeit.service.basic;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelAlreadyExistException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @Test
  void create_success_binaryContent() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("content", channelId, authorId);

    Channel channel = new Channel(ChannelType.PUBLIC, "channel", "desc");
    User user = new User("user", "user@test.com", "1234", null);
    Message message = new Message("content", channel, user, List.of());

    UserDto userDto = new UserDto(authorId, user.getUsername(), user.getEmail(), null, true);
    MessageDto messageDto = new MessageDto(
        messageId, Instant.now(), Instant.now(), "content", channelId, userDto, List.of()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(messageRepository.save(any())).willReturn(message);
    given(messageMapper.toDto(any())).willReturn(messageDto);

    MessageDto result = messageService.create(request, List.of());

    then(messageRepository).should().save(any());

    assertNotNull(result);
    assertEquals("content", result.content());
    assertEquals(channelId, result.channelId());
    assertEquals(userDto.email(), result.author().email());
    assertTrue(result.attachments().isEmpty());
  }

  @Test
  void update_success() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("update content");

    Channel channel = new Channel(ChannelType.PUBLIC, "채널", "설명");
    User user = new User("user", "user@test.com", "1234", null);
    Message message = new Message("original content", channel, user, List.of());

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willReturn(
        new MessageDto(
            messageId, Instant.now(), Instant.now(), "updated content",
            UUID.randomUUID(),
            new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, true),
            List.of()
        )
    );

    MessageDto result = messageService.update(messageId, request);

    then(messageRepository).should().findById(messageId);
    then(messageMapper).should().toDto(message);

    assertEquals("updated content", result.content());
  }
}