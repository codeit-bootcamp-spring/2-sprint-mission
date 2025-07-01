package com.sprint.mission.discodeit.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.message.MessageException;
import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.message.service.MessageService;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MessageIntegrationTest {


  @Autowired
  private MessageService messageService;

  @Autowired
  private JpaMessageRepository messageRepository;

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private JpaChannelRepository channelRepository;


  private UUID userId;
  private UUID channelId;
  private UUID m1Id;
  private UUID m2Id;


  @BeforeEach
  void setUp() {
    User user = User.create("user", "user@user.com", "user123", null);
    user.setUserStatus(UserStatus.create(user, Instant.now()));
    userId = userRepository.save(user).getId();
    Channel channel = Channel.create("channel", "channel123", ChannelType.PUBLIC);
    channelId = channelRepository.save(channel).getId();

    Message m1 = Message.create(user, channel, "a", List.of());
    Message m2 = Message.create(user, channel, "b", List.of());

    m1Id = messageRepository.save(m1).getId();
    m2Id = messageRepository.save(m2).getId();
  }

  @Test
  void Create_NoImage() {
    // given
    MessageCreateRequest createCommand = new MessageCreateRequest(userId, channelId, "test");
    // when
    MessageDto messageDto = messageService.create(createCommand, List.of());
    // then
    assertNotNull(messageDto.id());
    assertEquals(userId, messageDto.author().id());
    assertEquals("user", messageDto.author().username());
    assertEquals(channelId, messageDto.channelId());
    assertEquals("test", messageDto.content());
    Optional<Message> optionalMessage = messageRepository.findById(messageDto.id());
    assertTrue(optionalMessage.isPresent());

  }

  @Test
  void Create_WithImage() {
    // given
    MessageCreateRequest createCommand = new MessageCreateRequest(userId, channelId, "test");
    BinaryContentCreateRequest binaryContentCreateRequest1 = new BinaryContentCreateRequest(
        "test1.png", "image/png", new byte[0]);
    BinaryContentCreateRequest binaryContentCreateRequest2 = new BinaryContentCreateRequest(
        "test2.png", "image/png", new byte[0]);
    // when
    MessageDto messageDto = messageService.create(createCommand,
        List.of(binaryContentCreateRequest1, binaryContentCreateRequest2));
    // then
    assertNotNull(messageDto.id());
    assertEquals(userId, messageDto.author().id());
    assertEquals("user", messageDto.author().username());
    assertEquals(channelId, messageDto.channelId());
    assertEquals("test", messageDto.content());

    assertThat(messageDto.attachments()).hasSize(2);
    assertThat(messageDto.attachments().get(0).fileName()).isEqualTo(
        binaryContentCreateRequest1.fileName());

    Optional<Message> optionalMessage = messageRepository.findById(messageDto.id());
    assertTrue(optionalMessage.isPresent());
  }

  @Test
  void update_Success() {
    // given
    MessageUpdateRequest request = new MessageUpdateRequest("aaaa");
    // when
    MessageDto messageDto = messageService.update(m1Id, request);
    // then
    assertNotNull(messageDto.id());
    assertEquals(userId, messageDto.author().id());
    assertEquals("user", messageDto.author().username());
    assertEquals(channelId, messageDto.channelId());
    assertEquals("aaaa", messageDto.content());
    assertNotEquals("a", messageDto.content());
  }

  @Test
  void update_WithoutMessage_Throw404() {
    // given
    UUID fakeId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("aaaa");
    // when & then
    assertThrows(MessageException.class, () -> {
      messageService.update(fakeId, request);
    });
  }

  @Test
  void delete_Success() {
    // when
    messageService.delete(m1Id);
    // then
    Optional<Message> optionalMessage = messageRepository.findById(m1Id);
    assertFalse(optionalMessage.isPresent());
  }

  @Test
  void delete_WithoutMessage_Throw404() {
    // given
    UUID fakeId = UUID.randomUUID();
    // when & then
    assertThrows(MessageException.class, () -> {
      messageService.delete(fakeId);
    });
    Optional<Message> optionalMessage = messageRepository.findById(fakeId);
    assertFalse(optionalMessage.isPresent());
  }

  @Test
  void findAllByChannelId_Success() {
    // given
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "createdAt"));
    // when
    Slice<MessageDto> messageDtoSlice = messageService.findAllByChannelId(channelId, now, pageable);
    // then
    for (MessageDto messageDto : messageDtoSlice) {
      assertNotNull(messageDto.id());
    }
    assertThat(messageDtoSlice.getContent().get(0).content()).isEqualTo("a");
  }

  @Test
  void findAll_WithoutChannel_ShouldReturnEmptyList() {
    // given
    UUID fakeId = UUID.randomUUID();
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "createdAt"));
    // when
    Slice<MessageDto> messageDtoSlice = messageService.findAllByChannelId(fakeId, now, pageable);
    // then
    assertThat(messageDtoSlice).hasSize(0);
    assertThrows(IndexOutOfBoundsException.class, () -> messageDtoSlice.getContent().get(0));
  }
}
