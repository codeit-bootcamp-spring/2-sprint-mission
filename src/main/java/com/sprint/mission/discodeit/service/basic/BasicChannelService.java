package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel newChannel = new Channel(type, name, description);
        return channelRepository.save(newChannel);
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel existingChannel = channelRepository.findById(channelId);
        existingChannel.update(newName, newDescription);
        return channelRepository.update(existingChannel);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }

    @Override
    public boolean exists(UUID channelId) {
        return false;
    }
}
