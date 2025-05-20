package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "classpath:schema.sql")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Message createMessage(Channel channel) {
        return Message.builder()
                .content("Test Message")
                .channel(channel)
                .build();
    }

    private Channel createChannel() {
        Channel channel = Channel.builder()
                .name("Test Channel")
                .type(ChannelType.PUBLIC)
                .build();
        return entityManager.persist(channel);
    }

    // 성공 케이스: 채널 ID로 메시지 조회
    @Test
    void findAllByChannelId_Success() {
        // Given
        Channel channel = createChannel();
        Message message = entityManager.persist(createMessage(channel));
        entityManager.flush();
        entityManager.clear();

        // When
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());

        // Then
        assertThat(messages)
                .hasSize(1)
                .extracting(Message::getId)
                .containsExactly(message.getId());
    }

    // 페이징 및 정렬 테스트
    @Test
    void findAllByChannelIdOrderByCreatedAtDesc_Paging() {
        // Given
        Channel channel = createChannel();
        Message message1 = entityManager.persist(createMessage(channel));
        Message message2 = entityManager.persist(createMessage(channel));
        entityManager.flush();
        entityManager.clear();

        // When
        Slice<Message> slice = messageRepository.findAllByChannelIdOrderByCreatedAtDesc(
                channel.getId(),
                PageRequest.of(0, 1, Sort.by("createdAt").descending())
        );

        // Then
        assertThat(slice.getContent())
                .hasSize(1)
                .extracting(Message::getId)
                .containsExactly(message2.getId());
    }

    // 채널 객체로 삭제 테스트
    @Test
    void deleteAllByChannel_Success() {
        // Given
        Channel channel = createChannel();
        entityManager.persist(createMessage(channel));
        entityManager.flush();

        // When
        messageRepository.deleteAllByChannel(channel);
        entityManager.flush();

        // Then
        assertThat(messageRepository.count()).isZero();
    }
}