package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

  // --- create() ---

  @Test
  void givenValidRequest_whenCreate_thenReturnsDto() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    var req = new MessageCreateRequest("Hello", channelId, authorId);
    List<BinaryContentCreateRequest> attachments = List.of();

    Channel channel = new Channel(ChannelType.PUBLIC, "n", "d");
    User author = new User("user", "u@example.com", "pw", null);
    Message saved = new Message("Hello", channel, author, List.of());
    MessageDto expected = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "Hello",
        channel.getId(),
        new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, false),
        List.of()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(author));
    given(messageRepository.save(any(Message.class))).willReturn(saved);
    given(messageMapper.toDto(any(Message.class))).willReturn(expected);

    MessageDto result = messageService.create(req, attachments);

    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(authorId);
    then(messageRepository).should().save(any(Message.class));
    then(messageMapper).should().toDto(any(Message.class));
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void givenUnknownChannel_whenCreate_thenThrowsChannelNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    var req = new MessageCreateRequest("Hi", channelId, authorId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.create(req, List.of()))
        .isInstanceOf(ChannelNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("channelId", channelId);

    then(channelRepository).should().findById(channelId);
    then(userRepository).should(never()).findById(any());
    then(messageRepository).should(never()).save(any());
  }

  @Test
  void givenUnknownAuthor_whenCreate_thenThrowsUserNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    var req = new MessageCreateRequest("Hi", channelId, authorId);

    Channel channel = new Channel(ChannelType.PUBLIC, "n", "d");
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.create(req, List.of()))
        .isInstanceOf(UserNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("authorId", authorId);

    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(authorId);
    then(messageRepository).should(never()).save(any());
  }

  @Test
  void givenExistingMessage_whenFind_thenReturnsDto() {
    UUID msgId = UUID.randomUUID();
    Message msg = new Message(
        "txt",
        new Channel(ChannelType.PUBLIC, "n", "d"),
        new User("u", "u@u", "pw", null),
        List.of()
    );
    MessageDto expected = new MessageDto(
        msgId,
        Instant.now(),
        Instant.now(),
        "txt",
        msg.getChannel().getId(),
        new UserDto(msg.getAuthor().getId(), msg.getAuthor().getUsername(),
            msg.getAuthor().getEmail(), null, false),
        List.of()
    );

    given(messageRepository.findById(msgId)).willReturn(Optional.of(msg));
    given(messageMapper.toDto(any(Message.class))).willReturn(expected);

    MessageDto result = messageService.find(msgId);

    then(messageRepository).should().findById(msgId);
    then(messageMapper).should().toDto(any(Message.class));
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void givenUnknownMessage_whenFind_thenThrowsNotFound() {
    UUID msgId = UUID.randomUUID();
    given(messageRepository.findById(msgId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.find(msgId))
        .isInstanceOf(MessageNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("messageId", msgId);

    then(messageRepository).should().findById(msgId);
  }

  @Test
  void givenSlice_whenFindAllByChannelId_thenReturnsPageResponse() {
    // given
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now().minusSeconds(60);
    PageRequest pageable = PageRequest.of(0, 10);

    Channel channel = new Channel(ChannelType.PUBLIC, "n", "d");
    User author = new User("u", "u@u", "pw", null);
    Message m1 = new Message("c1", channel, author, List.of());
    Message m2 = new Message("c2", channel, author, List.of());
    PageImpl<Message> slice = new PageImpl<>(List.of(m1, m2), pageable, 2);

    MessageDto dto1 = new MessageDto(
        UUID.randomUUID(), m1.getCreatedAt(), m1.getUpdatedAt(),
        "c1", channelId,
        new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, false),
        List.of()
    );
    MessageDto dto2 = new MessageDto(
        UUID.randomUUID(), m2.getCreatedAt(), m2.getUpdatedAt(),
        "c2", channelId,
        new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, false),
        List.of()
    );

    PageResponse<MessageDto> expectedPage = new PageResponse<>(
        List.of(dto1, dto2),
        dto2.createdAt(),
        pageable.getPageSize(),
        slice.hasNext(),
        slice.getTotalElements()
    );

    given(messageRepository.findAllByChannelIdWithAuthor(channelId, cursor, pageable))
        .willReturn(slice);
    given(messageMapper.toDto(m1)).willReturn(dto1);
    given(messageMapper.toDto(m2)).willReturn(dto2);
    given(pageResponseMapper.fromSlice(any(Slice.class), eq(dto2.createdAt())))
        .willReturn(expectedPage);

    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor,
        pageable);

    then(messageRepository).should().findAllByChannelIdWithAuthor(channelId, cursor, pageable);
    then(pageResponseMapper).should().fromSlice(any(Slice.class), eq(dto2.createdAt()));
    assertThat(result).isEqualTo(expectedPage);
  }

  @Test
  void givenExistingMessage_whenUpdate_thenReturnsDto() {
    UUID msgId = UUID.randomUUID();
    var req = new MessageUpdateRequest("newContent");

    Channel channel = new Channel(ChannelType.PUBLIC, "chan", "desc");
    User author = new User("aut", "aut@example.com", "pw", null);
    Message existing = new Message("oldContent", channel, author, List.of());

    given(messageRepository.findById(msgId)).willReturn(Optional.of(existing));

    MessageDto expectedDto = new MessageDto(
        msgId,
        Instant.now(),
        Instant.now(),
        "newContent",
        channel.getId(),
        new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, false),
        List.of()
    );
    given(messageMapper.toDto(any(Message.class))).willReturn(expectedDto);

    MessageDto result = messageService.update(msgId, req);

    then(messageRepository).should().findById(msgId);
    then(messageMapper).should().toDto(any(Message.class));
    assertThat(result).isEqualTo(expectedDto);
  }


  @Test
  void givenUnknownMessage_whenUpdate_thenThrowsNotFound() {
    UUID msgId = UUID.randomUUID();
    given(messageRepository.findById(msgId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.update(msgId, new MessageUpdateRequest("x")))
        .isInstanceOf(MessageNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("messageId", msgId);

    then(messageRepository).should().findById(msgId);
    then(messageMapper).should(never()).toDto(any());
  }
  
  @Test
  void givenExistingMessage_whenDelete_thenDeletesSuccessfully() {
    UUID msgId = UUID.randomUUID();
    given(messageRepository.existsById(msgId)).willReturn(true);

    messageService.delete(msgId);

    then(messageRepository).should().existsById(msgId);
    then(messageRepository).should().deleteById(msgId);
  }

  @Test
  void givenUnknownMessage_whenDelete_thenThrowsNotFound() {
    UUID msgId = UUID.randomUUID();
    given(messageRepository.existsById(msgId)).willReturn(false);

    assertThatThrownBy(() -> messageService.delete(msgId))
        .isInstanceOf(MessageNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("messageId", msgId);

    then(messageRepository).should().existsById(msgId);
  }
}
