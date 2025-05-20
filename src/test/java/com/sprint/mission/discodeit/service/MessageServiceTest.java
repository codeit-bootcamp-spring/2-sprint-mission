package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
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

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService messageService;

  @DisplayName("메시지 생성 테스트 - 성공")
  @Test
  void createMessageSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    CreateMessageRequest request = new CreateMessageRequest("test", channelId, userId);

    Channel channel = mock(Channel.class);
    User user = mock(User.class);

    Message message = new Message(user, channel, "test");
    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        null,
        "test",
        channelId,
        null,
        List.of()
    );

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = messageService.createMessage(request);

    // then
    assertThat(result).isEqualTo(messageDto);
    assertThat(result.content()).isEqualTo(messageDto.content());
  }

  @DisplayName("메시지 생성 테스트 - 존재하지 않는 유저 실패")
  @Test
  void createMessageUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    CreateMessageRequest request = new CreateMessageRequest("test", channelId, userId);

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> messageService.createMessage(request))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.name());
  }

  @DisplayName("메시지 생성 테스트 - 존재하지 않는 채널 실패")
  @Test
  void createMessageChannelNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    CreateMessageRequest request = new CreateMessageRequest("test", channelId, userId);

    User user = mock(User.class);

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> messageService.createMessage(request))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining(ErrorCode.CHANNEL_NOT_FOUND.name());
  }


  @DisplayName("메시지 업데이트 테스트 - 성공")
  @Test
  void updateMessageSuccess() {
    // given

    UUID messageId = UUID.randomUUID();
    UpdateMessageRequest request = new UpdateMessageRequest("newcontent");

    User user = mock(User.class);
    Channel channel = mock(Channel.class);

    Message message = new Message(user, channel, "content");
    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        null,
        "newcontent",
        UUID.randomUUID(),
        null,
        List.of()
    );

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = messageService.updateMessage(messageId, request);

    // then
    assertThat(result.content()).isEqualTo(messageDto.content());
  }

  @DisplayName("메시지 업데이트 테스트 - 존재하지 않는 메시지 실패")
  @Test
  void updateMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    UpdateMessageRequest request = new UpdateMessageRequest("newcontent");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> messageService.updateMessage(messageId, request))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.MESSAGE_NOT_FOUND.name());
  }

  @DisplayName("메시지 삭제 테스트 - 성공")
  @Test
  void deleteMessageSuccess() {
    // given
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId)).willReturn(true);

    // when
    messageService.deleteMessage(messageId);

    // then
    then(messageRepository).should().deleteById(messageId);
  }

  @DisplayName("메시지 삭제 테스트 - 존재하지 않는 메시지 실패")
  @Test
  void deleteMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId)).willReturn(false);

    // when
    // then
    assertThatThrownBy(() -> messageService.deleteMessage(messageId))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.MESSAGE_NOT_FOUND.name());
  }
}
