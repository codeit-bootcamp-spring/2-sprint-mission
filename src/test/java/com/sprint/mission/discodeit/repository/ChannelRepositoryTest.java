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
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private User privateUser;
    private User publicUser;
    private Channel publicChannel;
    private Channel privateChannel;
    private ReadStatus readStatus;

    @BeforeEach
    void setUp() {
        readStatusRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();

        privateUser = new User("user1", "pw1", "user1@test.com", null);
        userRepository.save(privateUser);
        publicUser = new User("user2", "pw2", "user2@test.com", null);
        userRepository.save(publicUser);

        publicChannel = new Channel("공개", "", ChannelType.PUBLIC);
        channelRepository.save(publicChannel);
        privateChannel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(privateChannel);

        readStatus = new ReadStatus(privateUser, privateChannel, Instant.now());
        readStatusRepository.save(readStatus);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 공개_사용자_조회() {
        List<Channel> channels = channelRepository.findAllByUser(publicUser.getId());

        assertThat(channels).hasSize(1);
    }

    @Test
    void 비공개_사용자_조회() {
        List<Channel> channels = channelRepository.findAllByUser(privateUser.getId());

        assertThat(channels).hasSize(2);
    }

}