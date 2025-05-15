package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
}
