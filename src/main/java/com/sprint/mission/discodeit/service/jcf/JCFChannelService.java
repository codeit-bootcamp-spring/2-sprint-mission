package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.infra.ChannelRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository = new JCFChannelRepository();

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository.save(
                new Channel(name, owner.id())
        );

        return new ChannelDto(channel.getId(), channel.getName());
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id);

        return new ChannelDto(channel.getId(), channel.getName());
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(channel -> new ChannelDto(channel.getId(), channel.getName()))
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
