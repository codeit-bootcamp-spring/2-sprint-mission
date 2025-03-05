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

    public Channel getChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            System.out.println("Channel with ID " + id + " not found.");
            return null;
        }
        return channel;
    }


    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID id, String newName) {
        Channel channel = getChannel(id); // 예외 처리된 메서드 활용
        channel.updateChannelName(newName);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        getChannel(id); // 존재 여부 체크
        channelRepository.delete(id);
    }
}
