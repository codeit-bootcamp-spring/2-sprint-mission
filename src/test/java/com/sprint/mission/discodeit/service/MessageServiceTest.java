package com.sprint.mission.discodeit.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.ChannelNotFoundForMessageException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;


  @Mock
  private BinaryContentMapper binaryContentMapper;

  @Mock
  private UserMapper userMapper;

  @Test
  void createMessage_success() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);
    Channel channel = new Channel(ChannelType.PUBLIC, "test", "desc");
    User author = new User("user", "email", "pwd", null);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(author));

    Message savedMessage = new Message("내용", channel, author, List.of());
    given(messageRepository.save(any())).willReturn(savedMessage);
    given(messageMapper.toDto(any())).willReturn(new MessageDto(UUID.randomUUID(), null, null,
        "내용", channelId, userMapper.toDto(author),
        binaryContentRepository.findAll().stream()
            .map(en -> {
              return binaryContentMapper.toDto(en);
            })
            .toList()));

    // when
    MessageDto result = messageService.create(request, List.of());

    // then
    then(messageRepository).should().save(any());
    assertEquals("내용", result.content());
  }

  @Test
  void createMessage_fail_channelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when + then
    assertThrows(ChannelNotFoundForMessageException.class, () -> {
      messageService.create(request, List.of());
    });
  }
}
