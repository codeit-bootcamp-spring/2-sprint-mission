package com.sprint.mission.discodeit.message.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.channel.ChannelException;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.message.MessageException;
import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.message.service.BasicMessageService;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {

  @Mock
  private JpaUserRepository userRepository;
  @Mock
  private JpaChannelRepository channelRepository;
  @Mock
  private JpaMessageRepository messageRepository;
  @Mock
  private BinaryContentService binaryContentService;

  @InjectMocks
  private BasicMessageService messageService;

  User user;
  Channel channel;
  BinaryContentCreateRequest binaryCommand;

  @BeforeEach
  void setUp() {
    user = spy(User.class);
    channel = spy(Channel.class);
  }

  @Test
  void MessageCreateSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    MessageCreateRequest command = new MessageCreateRequest(userId, channelId, "test");

    BinaryContent dummyContent = mock(BinaryContent.class);
    binaryCommand = mock(BinaryContentCreateRequest.class);
    when(dummyContent.getId()).thenReturn(UUID.randomUUID());
    when(binaryContentService.create(binaryCommand)).thenReturn(dummyContent);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    // when
    MessageDto result = messageService.create(command, List.of(binaryCommand));
    // then
    assertThat(result.content()).isEqualTo("test");
  }

  @Test
  void MessageCreate_WithoutUser_ShouldThrowException() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest command = new MessageCreateRequest(userId, channelId, "test");
    BinaryContentCreateRequest binaryCommand = mock(BinaryContentCreateRequest.class);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(UserException.class,
        () -> messageService.create(command, List.of(binaryCommand)));

    verifyNoInteractions(channelRepository);
    verifyNoInteractions(messageRepository);
  }

  @Test
  void MessageCreate_WithoutChannel_ShouldThrowException() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest command = new MessageCreateRequest(userId, channelId, "test");
    BinaryContentCreateRequest binaryCommand = mock(BinaryContentCreateRequest.class);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelException.class,
        () -> messageService.create(command, List.of(binaryCommand)));

    verifyNoInteractions(messageRepository);
  }

  @Test
  void MessageUpdateSuccess() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = Message.create(user, channel, "test", List.of());
    MessageUpdateRequest request = new MessageUpdateRequest("abcdefg");

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    // when
    MessageDto update = messageService.update(messageId, request);
    // then
    assertThat(update.content()).isEqualTo("abcdefg");
  }

  @Test
  void MessageUpdate_WithoutMessage_ShouldThrowException() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest command = new MessageUpdateRequest("abcdefg");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(MessageException.class, () -> {
      messageService.update(messageId, command);
    });
  }

  @Test
  void MessageDeleteTestSuccess() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = Message.create(user, channel, "test", List.of());
    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    // when
    messageService.delete(messageId);
    // then
    verify(messageRepository).delete(message);
  }

  @Test
  void MessageDelete_WithoutMessage_ShouldThrowException() {
    // given
    UUID messageId = UUID.randomUUID();
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(MessageException.class, () -> {
      messageService.delete(messageId);
    });
  }

  @Test
  void MessageFindByChannelIdSuccess() {
    // given
    UUID channelId = UUID.randomUUID();

    Message message1 = Message.create(user, channel, "hello", List.of());
    Message message2 = Message.create(user, channel, "world", List.of());

    when(messageRepository.findByChannel_Id(channelId)).thenReturn(List.of(message1, message2));
    // when
    List<MessageDto> results = messageService.findByChannelId(channelId);
    // then
    assertThat(results.size()).isEqualTo(2);
    assertThat(results.get(0).content()).isEqualTo("hello");
    assertThat(results.get(1).content()).isEqualTo("world");
  }
}
