package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
class BasicMessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @Test
  @DisplayName("정상적인 메시지 생성 요청 테스트")
  void createMessage_success() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    String content = "테스트 메시지";

    Channel channel = new Channel(ChannelType.PUBLIC, "채널이름", "채널설명");
    User user = new User("테스트유저", "test@test.com", "password", null);

    MessageCreateRequest request = new MessageCreateRequest(content, channelId, authorId);
    BinaryContentCreateRequest binaryReq = new BinaryContentCreateRequest("test.png", "image/png",
        "abc".getBytes());
    List<BinaryContentCreateRequest> binaryRequests = List.of(binaryReq);

    BinaryContent savedContent = new BinaryContent("test.png", 3L, "image/png");
    Message savedMessage = new Message(content, channel, user, List.of(savedContent));
    MessageDto expectedDto = new MessageDto(
        UUID.randomUUID(), null, null, content, channelId, null, List.of()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(binaryContentRepository.save(any())).willReturn(savedContent);
    given(messageRepository.save(any())).willReturn(savedMessage);
    given(messageMapper.toDto(any())).willReturn(expectedDto);

    MessageDto result = messageService.create(request, binaryRequests);

    assertThat(result.content()).isEqualTo(content);
    verify(binaryContentStorage).put(any(), any());
    verify(messageRepository).save(any());
    verify(messageMapper).toDto(any());
  }

  @Test
  @DisplayName("채널이 존재하지 않을 때 예외 발생")
  void createMessage_channelNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);

    assertThatThrownBy(() -> messageService.create(request, List.of()))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("작성자가 존재하지 않을 때 예외 발생")
  void createMessage_userNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "채널", "설명");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);

    assertThatThrownBy(() -> messageService.create(request, List.of()))
        .isInstanceOf(UserNotFoundException.class);
  }
}