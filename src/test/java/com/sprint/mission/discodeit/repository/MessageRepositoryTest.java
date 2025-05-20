package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "testuser@naver.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_CHANNEL_NAME = "test-channel";
    private static final String TEST_CHANNEL_DESCRIPTION = "Test Channel";
    private static final String FIRST_MESSAGE_CONTENT = "First message";
    private static final String MIDDLE_MESSAGE_CONTENT = "Middle message";
    private static final String LATEST_MESSAGE_CONTENT = "Latest message";

    private User user;
    private Channel channel;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        channel = createChannel(ChannelType.PUBLIC, TEST_CHANNEL_NAME, TEST_CHANNEL_DESCRIPTION);

        message1 = createMessage(FIRST_MESSAGE_CONTENT, channel, user);
        pauseExecution(10);
        
        message2 = createMessage(MIDDLE_MESSAGE_CONTENT, channel, user);
        pauseExecution(10);
        
        message3 = createMessage(LATEST_MESSAGE_CONTENT, channel, user);
    }

    private User createUser(String username, String email, String password) {
        User user = new User(username, email, password, null);
        setCreatedAt(user);
        entityManager.persist(user);
        return user;
    }

    private Channel createChannel(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        setCreatedAt(channel);
        entityManager.persist(channel);
        return channel;
    }

    private Message createMessage(String content, Channel channel, User author) {
        Message message = new Message(content, channel, author, null);
        setCreatedAt(message);
        entityManager.persist(message);
        entityManager.flush();
        return message;
    }

    private void setCreatedAt(BaseEntity entity) {
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(entity, Instant.now());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set createdAt field: " + e.getMessage(), e);
        }
    }

    private void pauseExecution(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("채널의 가장 최근 메시지 찾기")
    void findTopByChannelIdOrderByCreatedAtDesc_Success() {
        Optional<Message> latestMessage = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            channel.getId());

        assertThat(latestMessage).isPresent();
        assertThat(latestMessage.get().getContent()).isEqualTo(LATEST_MESSAGE_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 채널의 최근 메시지 찾기")
    void findTopByChannelIdOrderByCreatedAtDesc_NotFound() {
        UUID nonExistentChannelId = UUID.randomUUID();

        Optional<Message> latestMessage = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            nonExistentChannelId);

        assertThat(latestMessage).isEmpty();
    }

    @Test
    @DisplayName("채널 ID와 생성 시간으로 메시지 찾기")
    void findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc_Success() {
        Instant now = Instant.now();
        Pageable pageable = PageRequest.of(0, 10);

        List<Message> messages = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
            channel.getId(), now, pageable);

        assertThat(messages).isNotEmpty();
        verifyMessagesInDescendingOrder(messages);
    }

    @Test
    @DisplayName("채널 ID로 페이징하여 메시지 찾기")
    void findByChannelIdOrderByCreatedAtDesc_Success() {
        Pageable pageable = PageRequest.of(0, 2);

        List<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(
            channel.getId(), pageable);

        assertThat(messages).hasSize(2);
        verifyMessagesInDescendingOrder(messages);
    }

    @Test
    @DisplayName("작성자 ID로 메시지 찾기")
    void findByAuthorId_Success() {
        List<Message> messages = messageRepository.findByAuthorId(user.getId());

        assertThat(messages).hasSize(3);
        assertThat(messages).extracting(Message::getContent)
            .containsExactlyInAnyOrder(FIRST_MESSAGE_CONTENT, MIDDLE_MESSAGE_CONTENT, LATEST_MESSAGE_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 작성자의 메시지 찾기")
    void findByAuthorId_NotFound() {
        UUID nonExistentUserId = UUID.randomUUID();

        List<Message> messages = messageRepository.findByAuthorId(nonExistentUserId);

        assertThat(messages).isEmpty();
    }

    @Test
    @DisplayName("채널 ID로 모든 메시지 삭제")
    void deleteAllByChannelId_Success() {
        messageRepository.deleteAllByChannelId(channel.getId());
        entityManager.flush();
        entityManager.clear();

        List<Message> remainingMessages = messageRepository.findByChannelIdOrderByCreatedAtDesc(
            channel.getId(), PageRequest.of(0, 10));
        assertThat(remainingMessages).isEmpty();
    }
    
    private void verifyMessagesInDescendingOrder(List<Message> messages) {
        for (int i = 0; i < messages.size() - 1; i++) {
            Instant current = messages.get(i).getCreatedAt();
            Instant next = messages.get(i + 1).getCreatedAt();
            assertThat(current).isAfterOrEqualTo(next);
        }
    }
} 