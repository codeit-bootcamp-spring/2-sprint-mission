package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(AppConfig.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Channel channel;
    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() throws InterruptedException {
        messageRepository.deleteAll();
        channelRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("user", "user.gmail.com", "password", null);
        UserStatus userStatus = new UserStatus(user, Instant.MIN);
        channel = new Channel(ChannelType.PUBLIC, "Public Channel", "This is a public channel.");
        message1 = new Message(user, channel, "1", null);
        Thread.sleep(5);
        message2 = new Message(user, channel, "2", null);

        userRepository.save(user);
        channelRepository.save(channel);
        messageRepository.save(message1);
        messageRepository.save(message2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("특정 채널에서 시간 이전 메시지들을 페이징으로 조회")
    void findAllByChannelIdWithAuthor_success() {
        var slice = messageRepository.findAllByChannelIdWithAuthor(
                channel.getId(),
                message2.getCreatedAt(),
                PageRequest.of(0, 2, Sort.by("createdAt").descending())
        );

        assertThat(slice.getContent()).hasSize(1);
        assertThat(slice.hasNext()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 채널 ID로 조회 시 빈 결과")
    void findAllByChannelIdWithAuthor_channelNotFound() {
        var slice = messageRepository.findAllByChannelIdWithAuthor(
                UUID.randomUUID(),
                Instant.now(),
                PageRequest.of(0, 2)
        );

        assertThat(slice.getContent()).isEmpty();
    }

    @Test
    @DisplayName("createdAt 이전에 메시지가 없으면 빈 결과")
    void findAllByChannelIdWithAuthor_createdAtTooEarly() {
        var slice = messageRepository.findAllByChannelIdWithAuthor(
                channel.getId(),
                Instant.now().minusSeconds(1000),
                PageRequest.of(0, 2)
        );

        assertThat(slice.getContent()).isEmpty();
    }

    @Test
    @DisplayName("채널 내 마지막 메시지 시간 조회 - 존재하는 경우")
    void findLastMessageAtByChannelId_success() {
        Optional<Instant> last = messageRepository.findLastMessageAtByChannelId(channel.getId());
        assertThat(last).isPresent();
    }

    @Test
    @DisplayName("채널 내 마지막 메시지 시간 조회 - 메시지가 없을 경우")
    void findLastMessageAtByChannelId_noMessages() {
        Channel emptyChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "empty", "desc"));
        Optional<Instant> last = messageRepository.findLastMessageAtByChannelId(emptyChannel.getId());
        assertThat(last).isEmpty();
    }
}
