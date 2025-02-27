package com.sprint.mission.discodeit.infra.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.infra.ChannelRepository;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private static final JCFChannelRepository jcfChannelRepository = new JCFChannelRepository();
    private static final Map<UUID, Channel> channels = new LinkedHashMap<>();

    private JCFChannelRepository() {
    }

    public static JCFChannelRepository getInstance() {
        return jcfChannelRepository;
    }

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);

        return findById(channel.getId());
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channels.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("[ERROR] 해당 id의 채널이 존재하지 않습니다");
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
