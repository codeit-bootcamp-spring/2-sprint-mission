package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService() {
        this.channelRepository = new FileChannelRepository();
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID id, String newName) {
        Channel channel = getChannel(id);
        channel.updateChannelName(newName);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        getChannel(id);
        channelRepository.delete(id);
    }
}
