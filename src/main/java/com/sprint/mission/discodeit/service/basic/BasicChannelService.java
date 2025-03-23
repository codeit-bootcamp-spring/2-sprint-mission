package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.DTO.channelService.ChannelCreateDTO;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;


    @Override
    public Channel create(ChannelCreateDTO channelCreateDto) {
        Channel channel = channelCreateDto.toEntity();
        return channelRepository.save(channel);
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
    public Channel update(UUID channelId, String newName, ChannelType newType) {
        return channelRepository.update(channelId, newName, newType);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
