package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileMessageRepositoryTest {

  private static final String MESSAGE_FILE = "message.ser";
  @TempDir
  private Path path;
  private MessageRepository messageRepository;
  private Message setUpMessage;
  private Message savedSetUpMessage;
  private UUID setUpChannelId;

  @BeforeEach
  void setUp() {
    messageRepository = new FileMessageRepository(path.resolve(MESSAGE_FILE));
    setUpChannelId = UUID.randomUUID();
    setUpMessage = new Message(MESSAGE_CONTENT, setUpChannelId, UUID.randomUUID(), List.of());
    savedSetUpMessage = messageRepository.save(setUpMessage);
  }

  @DisplayName("메세지를 저장할 경우, 저장한 메세지 반환합니다.")
  @Test
  void save() {
    assertThat(savedSetUpMessage.getId()).isEqualTo(setUpMessage.getId());
  }

  @DisplayName("메세지 ID를 조회할 경우, 해당 채널을 반환합니다.")
  @Test
  void findById() {
    Optional<Message> message = messageRepository.findByMessageId(
        savedSetUpMessage.getId());

    assertThat(message)
        .map(Message::getId)
        .hasValue(savedSetUpMessage.getId());
  }

  @DisplayName("전체 메세지를 조회할 경우, 전채 메세지를 반환합니다.")
  @Test
  void findAll() {
    Message otherMessage = messageRepository.save(
        new Message(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID(), List.of()));
    List<Message> messages = messageRepository.findAll();

    assertThat(messages).extracting(Message::getId)
        .containsExactlyInAnyOrder(savedSetUpMessage.getId(), otherMessage.getId());
  }

  @DisplayName("채널 ID로 조회할 경우, 해당 채널에 속하는 메세지 전체를 반환합니다.")
  @Test
  void findByChannelId() {
    Message otherMessage = messageRepository.save(
        new Message(MESSAGE_CONTENT, setUpChannelId, UUID.randomUUID(), List.of()));
    List<Message> messages = messageRepository.findByChannelId(setUpChannelId);

    assertThat(messages)
        .extracting(Message::getId)
        .containsExactlyInAnyOrder(savedSetUpMessage.getId(), otherMessage.getId());
  }

  @DisplayName("메세지를 삭제할 경우, 반환값은 없습니다.")
  @Test
  void delete() {
    messageRepository.delete(savedSetUpMessage.getId());
    Optional<Message> message = messageRepository.findByMessageId(
        savedSetUpMessage.getId());

    assertThat(message).isNotPresent();
  }

  @DisplayName("채널내 최신 메세지 생성시간을 조회할 경우, 해당 시간을 반환합니다.")
  @Test
  void findLastMessageCreatedAtByChannelId() {
    Message laterMessage =
        new Message(MESSAGE_CONTENT, setUpChannelId, UUID.randomUUID(), List.of());
    Message savedLater = messageRepository.save(laterMessage);

    Optional<Instant> lastMessageCreatedAtByChannelIdOptional = messageRepository.findLastMessageCreatedAtByChannelId(
        setUpChannelId);

    assertThat(lastMessageCreatedAtByChannelIdOptional)
        .hasValue(savedLater.getCreatedAt());
  }
}