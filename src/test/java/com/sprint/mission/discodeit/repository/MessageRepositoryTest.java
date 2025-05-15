package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("채널에서 마지막 메시지 시간 조회 성공")
    void findLastMessageAtByChannelId_success() {
        User user = new User("user1", "user@email.com", "1234", null);
        UserStatus status = new UserStatus(user, Instant.now());

        Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "설명");
        Message message = new Message("메시지 내용", channel, user, List.of());

        entityManager.persist(user);
        entityManager.persist(status);
        entityManager.persist(channel);
        entityManager.persist(message);
        entityManager.flush();
        entityManager.clear();

        Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());
        assertThat(result).isPresent();
    }
    @Test
    @DisplayName("채널에서 이전 시간 기준 메시지 페이징 조회 성공")
    void findAllByChannelIdWithAuthor_success() {
        User user = new User("user1", "user@email.com", "1234", null);
        UserStatus status = new UserStatus(user, Instant.now());

        Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "설명");
        entityManager.persist(user);
        entityManager.persist(status);
        entityManager.persist(channel);

        for (int i = 0; i < 3; i++) {
            Message msg = new Message("메시지 " + i, channel, user, List.of());
            entityManager.persist(msg);
        }

        entityManager.flush();
        entityManager.clear();

        Instant now = Instant.now();
        Pageable pageable = Pageable.ofSize(2);
        Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(), now, pageable);

        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0).getAuthor()).isNotNull();
    }
    @Test
    @DisplayName("createdAt보다 이후에 작성된 메시지만 존재할 경우 조회 결과 없음")
    void findAllByChannelIdWithAuthor_noMessagesBeforeTimestamp() {
        // given
        User user = new User("user1", "user@email.com", "1234", null);
        UserStatus status = new UserStatus(user, Instant.now());

        Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "설명");
        entityManager.persist(user);
        entityManager.persist(status);
        entityManager.persist(channel);

        Message message = new Message("미래 메시지", channel, user, List.of());
        entityManager.persist(message);

        entityManager.flush();
        entityManager.clear();

        Instant past = Instant.now().minusSeconds(3600);
        Pageable pageable = Pageable.ofSize(5);
        Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(), past, pageable);

        assertThat(result).isEmpty();
    }
}
