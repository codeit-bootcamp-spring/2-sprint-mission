package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jpa", matchIfMissing = true)
@Repository
@RequiredArgsConstructor
public class JpaChannelRepository  implements ChannelRepository {

    private final SpringDataChannelRepository channelRepository;

    @Override
    public Channel save(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return channelRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        channelRepository.deleteById(id);
    }
}
