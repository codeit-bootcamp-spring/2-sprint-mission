package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

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
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
        channel.updateChannelName(newName);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        getChannel(id);
        channelRepository.delete(id);
    }
}
