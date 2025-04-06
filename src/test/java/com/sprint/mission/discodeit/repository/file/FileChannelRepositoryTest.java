package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;
import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.*;
import static org.assertj.core.api.Assertions.assertThat;

class FileChannelRepositoryTest {

    private static final String CHANNEL_FILE = "channel.ser";
    @TempDir
    private Path path;
    private ChannelRepository channelRepository;
    private Channel setUpchannel;
    private Channel savedSetUpchannel;

    @BeforeEach
    void setUp() {
        channelRepository = new FileChannelRepository(path.resolve(CHANNEL_FILE));
        setUpchannel = new Channel(PUBLIC, CHANNEL_NAME, CHANNEL_DESCRIPTION);
        savedSetUpchannel = channelRepository.save(setUpchannel);
    }

    @DisplayName("채널 저장할 경우, 저장된 채널을 반환합니다.")
    @Test
    void save() {
        assertThat(setUpchannel.getId()).isEqualTo(savedSetUpchannel.getId());
    }

    @DisplayName("채널 ID로 조회할 경우, 같은 ID를 가진 채널을 반환합니다.")
    @Test
    void findByChannelId() {
        Optional<Channel> channel = channelRepository.findByChannelId(savedSetUpchannel.getId());

        assertThat(channel).map(Channel::getId)
                .hasValue(savedSetUpchannel.getId());
    }

    @DisplayName("전체 조회할 경우, 전체 채널을 반환합니다.")
    @Test
    void findAll() {
        Channel otherChannel = channelRepository.save(new Channel(PRIVATE, UPDATED_CHANNEL_NAME, CHANNEL_DESCRIPTION));
        List<Channel> channels = channelRepository.findAll();

        assertThat(channels).extracting(Channel::getId)
                .containsExactlyInAnyOrder(savedSetUpchannel.getId(), otherChannel.getId());
    }

    @DisplayName("채널을 삭제할 경우, 반환값은 없습니다.")
    @Test
    void delete() {
        channelRepository.delete(savedSetUpchannel.getId());
        Optional<Channel> channel = channelRepository.findByChannelId(setUpchannel.getId());

        assertThat(channel).isNotPresent();
    }
}