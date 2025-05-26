package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class ReadStatusRepositoryTest {
    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User createTestUser(String username, String email) {
        BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
        User user = new User(username, email, "password123!@#", profile);
        //UserStatus 생성 및 연결
        UserStatus status = new UserStatus(user, Instant.now());
        return userRepository.save(user);
    }

    private Channel createTestChannel(ChannelType type, String name) {
        Channel channel = new Channel(type, name, "설명: " + name);
        return channelRepository.save(channel);
    }

    private ReadStatus createTestReadStatus(User user, Channel channel, Instant lastReadAt) {
        ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
        return readStatusRepository.save(readStatus);
    }

    @Test
    @DisplayName("사용자 ID로 모든 읽음 상태를 조회할 수 있다")
    void findAllByUserId_ReturnsReadStatuses() {
        //given
        User user = createTestUser("testUser", "test@test.com");
        Channel channel1 = createTestChannel(ChannelType.PUBLIC, "채널1");
        Channel channel2 = createTestChannel(ChannelType.PRIVATE, "채널2");

        Instant now = Instant.now();
        ReadStatus readStatus1 = createTestReadStatus(user, channel1, now.minus(1, ChronoUnit.DAYS));
        ReadStatus readStatus2 = createTestReadStatus(user, channel2, now);

        entityManager.flush();
        entityManager.clear();

        //when
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(user.getId());

        //then
        assertThat(readStatuses).hasSize(2);
    }

    @Test
    @DisplayName("채널 ID로 모든 읽음 상태를 사용자 정보와 함께 조회할 수 있다")
    void findAllByChannelIdWithUser_ReturnsReadStatuses() {
        //given
        User user1 = createTestUser("user1", "user1@example.com");
        User user2 = createTestUser("user2", "user2@example.com");
        Channel channel = createTestChannel(ChannelType.PUBLIC, "공개채널");

        Instant now = Instant.now();
        ReadStatus readStatus1 = createTestReadStatus(user1, channel, now.minus(1, ChronoUnit.DAYS));
        ReadStatus readStatus2 = createTestReadStatus(user2, channel, now);

        entityManager.flush();
        entityManager.clear();

        //when
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdWithUser(channel.getId());

        //then
        assertThat(readStatuses).hasSize(2);

        //사용자 정보가 함께 로드되었는지 확인 (FETCH JOIN)
        for (ReadStatus status : readStatuses) {
            assertThat(Hibernate.isInitialized(status.getUser())).isTrue();
            assertThat(Hibernate.isInitialized(status.getUser().getStatus())).isTrue();
            assertThat(Hibernate.isInitialized(status.getUser().getProfile())).isTrue();
        }
    }

    @Test
    @DisplayName("사용자 ID와 채널 ID로 읽음 샅애 존재 여부를 확인할 수 있다")
    void existsByUserIdAndChannelId_ExistingStatus_ReturnsTrue() {
        //given
        User user = createTestUser("testUser", "test@example.com");
        Channel channel = createTestChannel(ChannelType.PUBLIC, "공개채널");

        ReadStatus readStatus = createTestReadStatus(user, channel, Instant.now());

        entityManager.flush();
        entityManager.clear();

        //when
        Boolean exists = readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않은 읽음 상태에 대해 false를 반환한다.")
    void existsByUserIdAndChannelId_NonExistingStatus_ReturnFalse() {
        //given
        User user = createTestUser("testUser", "test@example.com");
        Channel channel = createTestChannel(ChannelType.PUBLIC, "공개채널");

        entityManager.flush();
        entityManager.clear();

        //읽음 상태를 생성하지 않음

        //when
        Boolean exists = readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId());

        //then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("채널의 모든 읽음 상태를 삭제할 수 있다")
    void deleteAllByChannelId_DeletesAllReadStatuses() {
        //given
        User user1 = createTestUser("user1", "user1@example.com");
        User user2 = createTestUser("user2", "user2@example.com");

        Channel channel = createTestChannel(ChannelType.PUBLIC, "삭제할 채널");
        Channel otherChannel = createTestChannel(ChannelType.PUBLIC, "유지할 채널");

        //삭제할 채널에 읽음 상태 2개 생성
        createTestReadStatus(user1, channel, Instant.now());
        createTestReadStatus(user2, channel, Instant.now());

        //유지할 채널에 읽음 상태 1개 생성
        createTestReadStatus(user1, otherChannel, Instant.now());

        entityManager.flush();
        entityManager.clear();

        //when
        readStatusRepository.deleteAllByChannelId(channel.getId());
        entityManager.flush();
        entityManager.clear();

        //then
        //해당 채널의 읽음 상태는 삭제되었는지 확인
        List<ReadStatus> channelReadStatuses = readStatusRepository.findAllByChannelIdWithUser(channel.getId());
        assertThat(channelReadStatuses).isEmpty();

        //다른 채널의 읽음 상태는 그대로인지 확인
        List<ReadStatus> otherChannelReadStatuses = readStatusRepository.findAllByChannelIdWithUser(otherChannel.getId());
        assertThat(otherChannelReadStatuses).hasSize(1);
    }
}
