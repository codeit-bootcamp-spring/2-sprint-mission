package com.sprint.mission.discodeit.repository.jcf;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels = new LinkedHashMap<>();

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);

        return findById(channel.getId());
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channels.get(id);
        if (channel == null) {
            throw new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent());
        }

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channels.values()
                .stream()
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
