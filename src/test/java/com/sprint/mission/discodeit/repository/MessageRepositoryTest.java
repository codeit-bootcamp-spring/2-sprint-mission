package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;
    private Channel channel;
    List<BinaryContent> attachments;
    private BinaryContent attachment1;
    private BinaryContent attachment2;
    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        binaryContentRepository.deleteAllInBatch();
        messageRepository.deleteAllInBatch();

        user = new User("user", "pw", "user@test.com", null);
        userRepository.save(user);

        channel = new Channel("테스트 채널", "test", ChannelType.PUBLIC);
        channelRepository.save(channel);

        attachment1 = new BinaryContent("첨부파일1", 10L, "image/jpeg");
        binaryContentRepository.save(attachment1);
        attachment2 = new BinaryContent("첨부파일1", 10L, "image/jpeg");
        binaryContentRepository.save(attachment2);
        List<BinaryContent> attachments = Arrays.asList(attachment1, attachment2);

        message1 = new Message("테스트 메세지1", user, channel, null);
        messageRepository.save(message1);
        message2 = new Message("테스트 메세지2", user, channel, attachments);
        messageRepository.save(message2);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 채널_아이디_조회_테스트() {
        List<Message> messages = messageRepository.findByChannelId(channel.getId());

        assertThat(messages).hasSize(2);
    }

    @Test
    void 채널_슬라이스_조회_테스트() {
        int pageSize = 1;
        PageRequest pageable = PageRequest.of(0, pageSize,
            Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Message> slice = messageRepository.findSliceByChannelId(channel.getId(), pageable);

        assertThat(slice.getContent()).hasSize(pageSize);
        assertThat(slice.hasNext()).isTrue();
        assertThat(slice.getContent().get(0).getAuthor()).isNotNull();
        assertThat(slice.getContent().get(0).getAuthor().getStatus()).isNotNull();
    }

    @Test
    void 채널_슬라이스_이전시간_조회_테스트() {
        Instant cursor = message2.getCreatedAt(); // message2가 message1보다 나중에 생성되었다고 가정
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Message> slice = messageRepository.findSliceByChannelIdAndCreatedAtBefore(
            channel.getId(), pageable, cursor);

        List<Message> content = slice.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content).extracting("content").contains("테스트 메세지1"); // message1만 포함되어야 함
        assertThat(content).noneMatch(m -> m.getCreatedAt().isAfter(cursor));
    }
}