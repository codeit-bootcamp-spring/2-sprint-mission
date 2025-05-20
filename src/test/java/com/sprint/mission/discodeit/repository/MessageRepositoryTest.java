package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    private User testUser;
    private Channel testChannel;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(
            new User("testUser", "pw123", "tester@example.com", null)
        );

        testChannel = channelRepository.save(
            new Channel("general", "일반 채널", ChannelType.PUBLIC)
        );

        message1 = messageRepository.save(new Message(testUser, testChannel, "first", List.of()));
        message2 = messageRepository.save(new Message(testUser, testChannel, "second", List.of()));
        message3 = messageRepository.save(new Message(testUser, testChannel, "third", List.of()));
    }

    @Test
    @DisplayName("findAllByChannel(): 특정 채널의 메시지를 모두 조회")
    void findAllByChannel_shouldReturnMessages() {
        // when
        List<Message> result = messageRepository.findAllByChannel(testChannel);

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("deleteAllByChannelId(): 특정 채널의 메시지를 모두 삭제")
    void deleteAllByChannelId_shouldRemoveAllMessages() {
        // when
        messageRepository.deleteAllByChannelId(testChannel.getId());

        // then
        List<Message> result = messageRepository.findAllByChannel(testChannel);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findFirstPageByChannel(): 채널의 최신 메시지들을 페이지 단위로 조회")
    void findFirstPageByChannel_shouldReturnFirstPage() {
        // when
        List<Message> result = messageRepository.findFirstPageByChannel(testChannel,
            PageRequest.of(0, 2));

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCreatedAt()).isAfter(result.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("findFirstPageByChannel(): 유효 범위를 벗어난 페이지를 요청하면 빈 리스트 반환")
    void findFirstPageByChannel_withInvalidPage_shouldReturnEmpty() {
        // when
        List<Message> result = messageRepository.findFirstPageByChannel(testChannel,
            PageRequest.of(10, 2));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findNextPageByChannelAndCursor(): 커서보다 이전의 메시지들을 조회")
    void findNextPageByChannelAndCursor_shouldReturnPreviousMessages() {
        // when
        Instant cursor = message3.getCreatedAt(); // 가장 마지막 메시지 시간(최신)
        List<Message> result = messageRepository.findNextPageByChannelAndCursor(
            testChannel, cursor, PageRequest.of(0, 10)
        );

        // then
        assertThat(result).hasSize(2);

        // 각 메시지의 createdAt이 커서보다 이전인지
        assertThat(result).allSatisfy(m -> assertThat(m.getCreatedAt()).isBefore(cursor));
    }

    @Test
    @DisplayName("findNextPageByChannelAndCursor(): 가장 오래된 메시지 커서보다 이전 메시지는 없음")
    void findNextPageByChannelAndCursor_withOldestCursor_shouldReturnEmpty() {
        // when
        Instant cursor = message1.getCreatedAt();
        List<Message> result = messageRepository.findNextPageByChannelAndCursor(
            testChannel, cursor, PageRequest.of(0, 10)
        );

        // then
        assertThat(result).isEmpty();
    }
}
