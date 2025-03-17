package com.sprint.discodeit.service.file;

import com.sprint.discodeit.entity.Channel;
import com.sprint.discodeit.entity.ChannelType;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;


public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId.toString());
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findByAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId.toString());
        channel.update(newName, newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
