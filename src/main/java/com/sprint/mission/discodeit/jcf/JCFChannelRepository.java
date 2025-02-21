package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repo.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public ChannelDto create(String name) {
        Channel channel = new Channel(name);
        channels.put(channel.getId(), channel);

        return new ChannelDto(channel.getId(), channel.getName());
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channels.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("[ERROR] 해당 id의 채널이 존재하지 않습니다");
        }

        return new ChannelDto(channel.getId(), channel.getName());
    }

    @Override
    public List<ChannelDto> findAll() {
        return channels.values()
                .stream()
                .map(channel -> new ChannelDto(channel.getId(), channel.getName()))
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = channels.get(id);
        channel.updateName(name);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
