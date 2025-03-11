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
    public Channel findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new RuntimeException(channelId + " 채널을 찾을 수 없습니다.");
        }

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = findById(channelId);
        channel.update(newName, newDescription);

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = findById(channelId);
        channelRepository.delete(channel.getId());
    }
}
