package com.sprint.mission.discodeit.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.jpa.JpaChannelRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataChannelRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DataJpaTest
@ActiveProfiles("test")
public class JpaChannelRepositoryTest {
    @Autowired
    private SpringDataChannelRepository channelRepository;

    @Test
    @DisplayName("PUBLIC 채널 생성 및 조회 성공")
    void saveAndFindPublicChannel() {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC, "general", "일반 채널");
        Channel saved = channelRepository.save(channel);

        // when
        Optional<Channel> found = channelRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getType()).isEqualTo(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("PRIVATE 채널 생성 및 조회 성공")
    void saveAndFindPrivateChannel() {
        // given
        Channel channel = new Channel(ChannelType.PRIVATE, "secret", null);
        Channel saved = channelRepository.save(channel);

        // when
        Optional<Channel> found = channelRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("secret");
    }

    @Test
    @DisplayName("존재하지 않는 채널 조회 시 Optional.empty() 반환")
    void findById_notFound() {
        // when
        Optional<Channel> found = channelRepository.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("전체 채널 조회")
    void findAllChannels() {
        // given
        channelRepository.save(new Channel(ChannelType.PUBLIC, "channel1", null));
        channelRepository.save(new Channel(ChannelType.PRIVATE, "channel2", null));
        channelRepository.save(new Channel(ChannelType.PUBLIC, "channel3", null));

        // when
        List<Channel> channels = channelRepository.findAll();

        // then
        assertThat(channels.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteById() {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC, "temp", null);
        Channel saved = channelRepository.save(channel);
        UUID id = saved.getId();

        // when
        channelRepository.deleteById(id);

        // then
        assertThat(channelRepository.existsById(id)).isFalse();
    }

    @Test
    @DisplayName("채널 존재 여부 확인")
    void existsById() {
        // given
        Channel channel = new Channel(ChannelType.PRIVATE, "test", null);
        Channel saved = channelRepository.save(channel);

        // when
        boolean exists = channelRepository.existsById(saved.getId());

        // then
        assertThat(exists).isTrue();
    }

}
