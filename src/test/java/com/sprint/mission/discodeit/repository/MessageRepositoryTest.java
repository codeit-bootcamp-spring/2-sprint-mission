package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("findAllByChannelIdWithAuthor - 성공: 채널의 메시지를 author/프로필과 함께 슬라이스 조회")
    void findAllByChannelIdWithAuthor_success() {
        // given
        User author = userRepository.save(new User("author1", "author1@email.com", "pw1",null));
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "테스트채널", "채널A"));
        Message m1 = new Message("content1", channel, author, List.of());
        Message m2 = new Message("content2", channel, author, List.of());
        messageRepository.save(m1);
        messageRepository.save(m2);
        messageRepository.flush();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
                channel.getId(),
                m2.getCreatedAt().plusSeconds(1000),
                pageable
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getAuthor().getUsername()).isEqualTo("author1");
    }

    @Test
    @DisplayName("findLastMessageAtByChannelId - 성공: 채널의 마지막 메시지 시간 반환")
    void findLastMessageAtByChannelId_success() {
        // given
        User author = userRepository.save(new User("user2", "user2@email.com", "pw2", null));
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, "설명","채널B" ));
        Message m1 = new Message("msg1", channel, author, List.of());
        Message m2 = new Message("msg2", channel, author, List.of());
        messageRepository.save(m1);
        messageRepository.save(m2);
        messageRepository.flush();
        entityManager.clear();

        Message savedM2 = messageRepository.findById(m2.getId()).get();

// when
        Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId());

// then
        assertThat(lastMessageAt).isPresent();
        assertThat(lastMessageAt.get().toEpochMilli())
                .isEqualTo(savedM2.getCreatedAt().toEpochMilli());
    }

    @Test
    @DisplayName("findLastMessageAtByChannelId - 실패: 메시지 없는 채널은 empty 반환")
    void findLastMessageAtByChannelId_fail() {
        // given
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, "설명","채널C" ));

        // when
        Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId());

        // then
        assertThat(lastMessageAt).isEmpty();
    }

    @Test
    @DisplayName("deleteAllByChannelId - 성공: 특정 채널 메시지 전체 삭제")
    void deleteAllByChannelId_success() {
        // given
        User author = userRepository.save(new User("user3", "user3@email.com", "pw3",null));
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "설명", "채널D"));
        Message m1 = new Message("msg1", channel, author, List.of());
        Message m2 = new Message("msg2", channel, author, List.of());
        messageRepository.save(m1);
        messageRepository.save(m2);

        // when
        messageRepository.deleteAllByChannelId(channel.getId());

        // then
        assertThat(messageRepository.findAllByChannelIdWithAuthor(
                channel.getId(), Instant.now(), PageRequest.of(0, 10)
        ).getContent()).isEmpty();
    }
}
