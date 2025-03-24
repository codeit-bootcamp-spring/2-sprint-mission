package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final FileChannelRepository channelRepository;

    public FileChannelService() {
        this.channelRepository = new FileChannelRepository();
    }

    @Override
    public Channel createChannel(ChannelDTO channelDTO) {
        Channel channel = new Channel(channelDTO.channelName(), channelDTO.isPublic());
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
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
