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
    public Channel create(ChannelType type,String name,  String description) {
        Channel channel = new Channel(UUID.randomUUID(), name, type, description);
        channelRepository.save(channel);
        return channel;
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
    public Channel update(UUID channelId, String newName, ChannelType newType, String newDescription) {
        Channel channel = channelRepository.findById(channelId);
        if (channel != null) {
            channel.updateName(newName);
            channel.setType(newType);
            channel.setDescription(newDescription);
            channelRepository.save(channel);
        }
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
