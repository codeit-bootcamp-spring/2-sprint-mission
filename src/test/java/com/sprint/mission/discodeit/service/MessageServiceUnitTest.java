package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.message.usecase.BasicMessageService;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageUpdateCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
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
  BinaryContentCreateCommand binaryCommand;

  @BeforeEach
  void setUp() {
    user = spy(User.class);
    channel = spy(Channel.class);
    user.setUserStatus(mock(UserStatus.class));
  }

  @Test
  void MessageCreateSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    MessageCreateCommand command = new MessageCreateCommand(userId, channelId, "test");

    BinaryContent dummyContent = mock(BinaryContent.class);
    binaryCommand = mock(BinaryContentCreateCommand.class);
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

    MessageCreateCommand command = new MessageCreateCommand(userId, channelId, "test");
    BinaryContentCreateCommand binaryCommand = mock(BinaryContentCreateCommand.class);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(UserNotFoundException.class,
        () -> messageService.create(command, List.of(binaryCommand)));

    verifyNoInteractions(channelRepository);
    verifyNoInteractions(messageRepository);
  }

  @Test
  void MessageCreate_WithoutChannel_ShouldThrowException() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateCommand command = new MessageCreateCommand(userId, channelId, "test");
    BinaryContentCreateCommand binaryCommand = mock(BinaryContentCreateCommand.class);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class,
        () -> messageService.create(command, List.of(binaryCommand)));

    verifyNoInteractions(messageRepository);
  }

  @Test
  void MessageUpdateSuccess() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = Message.create(user, channel, "test", List.of());
    MessageUpdateCommand command = new MessageUpdateCommand(messageId, "abcdefg");

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    // when
    MessageDto update = messageService.update(command);
    // then
    assertThat(update.content()).isEqualTo("abcdefg");
  }

  @Test
  void MessageUpdate_WithoutMessage_ShouldThrowException() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateCommand command = new MessageUpdateCommand(messageId, "abcdefg");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(MessageNotFoundException.class, () -> {
      messageService.update(command);
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
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  void MessageDelete_WithoutMessage_ShouldThrowException() {
    // given
    UUID messageId = UUID.randomUUID();
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(MessageNotFoundException.class, () -> {
      messageService.delete(messageId);
    });
  }

  @Test
  void MessageFindByChannelIdSuccess() {
    // given
    UUID channelId = UUID.randomUUID();
    when(user.getUserStatus()).thenReturn(mock(UserStatus.class));

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
