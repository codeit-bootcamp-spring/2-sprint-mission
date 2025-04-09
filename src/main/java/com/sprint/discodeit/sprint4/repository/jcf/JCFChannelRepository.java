package com.sprint.discodeit.sprint4.repository.jcf;

import com.sprint.discodeit.sprint5.domain.entity.Channel;
import com.sprint.discodeit.sprint5.repository.file.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }


    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }


    @Override
    public Optional<Channel> findById(UUID uuID) {
        return Optional.ofNullable(data.get(uuID));

    }

    @Override
    public List<Channel> findByAll() {
        return data.values().stream().toList();
    }
}
