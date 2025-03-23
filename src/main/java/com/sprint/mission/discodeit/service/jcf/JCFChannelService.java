package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;
    private final JCFChannelRepository channelRepository;

    private JCFChannelService() {
        this.channelRepository = new JCFChannelRepository();
    }

    public static synchronized JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
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
    public void updateChannel(UUID id, String newChannelName) {
        Channel channel = getChannel(id);
        channel.updateChannelName(newChannelName);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        getChannel(id);
        channelRepository.delete(id);
    }
}
