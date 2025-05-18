package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@naver.com";
    private static final String PASSWORD = "123";
    private static final String PUBLIC_CHANNEL_NAME = "public channel";
    private static final String PUBLIC_CHANNEL_DESCRIPTION = "Public Channel";
    private static final String PRIVATE_CHANNEL_NAME = "private channel";
    private static final String PRIVATE_CHANNEL_DESCRIPTION = "Private Channel";
    private static final String NONEXISTENT_CHANNEL_NAME = "nonexistent channel";

    private User user;
    private Channel publicChannel;
    private Channel privateChannel;

    @BeforeEach
        //정렬을 위해 미리 추가 생성
    void setUp() {
        user = createTestUser();

        publicChannel = createChannel(ChannelType.PUBLIC, PUBLIC_CHANNEL_NAME,
            PUBLIC_CHANNEL_DESCRIPTION);
        privateChannel = createChannel(ChannelType.PRIVATE, PRIVATE_CHANNEL_NAME,
            PRIVATE_CHANNEL_DESCRIPTION);

        for (int i = 1; i <= 5; i++) {
            createChannel(
                ChannelType.PUBLIC,
                "public-channel-" + i,
                "Public Channel " + i
            );
        }

        createReadStatus(user, publicChannel);

        entityManager.flush();
    }

    private User createTestUser() {
        User user = new User(USERNAME, EMAIL, PASSWORD, null);
        entityManager.persist(user);
        return user;
    }

    private Channel createChannel(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        entityManager.persist(channel);
        return channel;
    }

    private ReadStatus createReadStatus(User user, Channel channel) {
        ReadStatus readStatus = new ReadStatus(user, channel);
        entityManager.persist(readStatus);
        return readStatus;
    }

    @Test
    @DisplayName("채널명으로 존재 여부 확인 - 존재함")
    void existsByName_True() {
        boolean exists = channelRepository.existsByName(PUBLIC_CHANNEL_NAME);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("채널명으로 존재 여부 확인 - 존재하지 않음")
    void existsByName_False() {
        boolean exists = channelRepository.existsByName(NONEXISTENT_CHANNEL_NAME);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("사용자 ID로 채널 목록 조회")
    void findChannelsByUserId_Success() {
        List<Channel> channels = channelRepository.findChannelsByUserId(user.getId());

        assertThat(channels).hasSize(1);
        assertThat(channels.get(0).getName()).isEqualTo(PUBLIC_CHANNEL_NAME);
    }

    @Test
    @DisplayName("사용자 ID로 채널 목록 조회 - 결과 없음")
    void findChannelsByUserId_Empty() {
        UUID nonExistentUserId = UUID.randomUUID();

        List<Channel> channels = channelRepository.findChannelsByUserId(nonExistentUserId);

        assertThat(channels).isEmpty();
    }

    @Test
    @DisplayName("채널 타입으로 목록 조회")
    void findAllByType_Success() {
        List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
        List<Channel> privateChannels = channelRepository.findAllByType(ChannelType.PRIVATE);

        assertThat(publicChannels).hasSize(6);
        assertThat(privateChannels).hasSize(1);
    }

    @Test
    @DisplayName("ID 목록과 타입으로 채널 목록 조회")
    void findAllByIdInAndType_Success() {
        List<UUID> channelIds = Arrays.asList(publicChannel.getId());

        List<Channel> channels = channelRepository.findAllByIdInAndType(
            channelIds,
            ChannelType.PUBLIC
        );

        assertThat(channels).hasSize(1);
        assertThat(channels.get(0).getName()).isEqualTo(PUBLIC_CHANNEL_NAME);
    }

    @Test
    @DisplayName("ID 목록과 타입으로 채널 목록 조회 - 결과 없음")
    void findAllByIdInAndType_Empty() {
        List<UUID> channelIds = Arrays.asList(publicChannel.getId());

        List<Channel> channels = channelRepository.findAllByIdInAndType(
            channelIds,
            ChannelType.PRIVATE
        );

        assertThat(channels).isEmpty();
    }

    @Test
    @DisplayName("채널 페이징 및 정렬")
    void findAll_WithPagingAndSorting() {
        PageRequest pageRequest = PageRequest.of(
            0,
            3,
            Sort.by("name").ascending()
        );

        Page<Channel> channelsPage = channelRepository.findAll(pageRequest);

        assertThat(channelsPage.getTotalElements()).isEqualTo(7);
        assertThat(channelsPage.getContent()).hasSize(3);
        assertThat(channelsPage.getTotalPages()).isEqualTo(3);

        List<Channel> channels = channelsPage.getContent();
        for (int i = 0; i < channels.size() - 1; i++) {
            assertThat(channels.get(i).getName()).isLessThanOrEqualTo(
                channels.get(i + 1).getName()
            );
        }
    }
} 