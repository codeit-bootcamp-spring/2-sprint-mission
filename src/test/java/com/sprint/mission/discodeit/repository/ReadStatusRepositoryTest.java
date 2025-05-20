package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ReadStatusRepositoryTest {

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user1;
    private User user2;
    private Channel channel1;
    private Channel channel2;
    private ReadStatus readStatus1;
    private ReadStatus readStatus2;
    private ReadStatus readStatus3;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        readStatusRepository.deleteAllInBatch();

        user1 = new User("user", "pw", "user@test.com", null);
        userRepository.save(user1);
        user2 = new User("user2", "pw2", "user2@test.com", null);
        userRepository.save(user2);

        channel1 = new Channel("channel", "test", ChannelType.PRIVATE);
        channelRepository.save(channel1);
        channel2 = new Channel("channel2", "test2", ChannelType.PRIVATE);
        channelRepository.save(channel2);

        readStatus1 = new ReadStatus(user1, channel1, Instant.now());
        readStatusRepository.save(readStatus1);
        readStatus2 = new ReadStatus(user2, channel1, Instant.now());
        readStatusRepository.save(readStatus2);
        readStatus3 = new ReadStatus(user2, channel2, Instant.now());
        readStatusRepository.save(readStatus3);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 채널_상태_사용자_조회_테스트() {
        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(user1.getId());
        System.out.println("채널 상태: " + readStatuses);
        assertThat(readStatuses.size()).isEqualTo(1);
    }

    @Test
    void 채널_상태_채널_조회_테스트() {
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel1.getId());
        assertThat(readStatuses.size()).isEqualTo(2);
    }

    @Test
    void 채널_상태_사용자_채널_조회() {
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(user2.getId(),
            channel2.getId()).orElseThrow();
        assertThat(readStatus.getId()).isEqualTo(readStatus3.getId());
    }

}