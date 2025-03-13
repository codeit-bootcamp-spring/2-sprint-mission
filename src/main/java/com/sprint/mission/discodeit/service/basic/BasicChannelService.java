package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        validateChannelField(type, name, description);
        Channel channel = new Channel(type, name, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = find(channelId);
        channel.update(newName, newDescription);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        find(channelId);
        channelRepository.deleteById(channelId);
    }

    private void validateChannelField(ChannelType type, String name, String description) {
        if(type == null || name == null || name.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException("type, name, description은 필수 입력값입니다.");
        }
    }
}
