package com.sprint.discodeit.repository.jcf;

import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.repository.ChannelRepository;
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
    public Channel findById(String uuID) {
        return Optional.ofNullable(data.get(UUID.fromString(uuID))).orElseThrow(() -> new NullPointerException(data.get(UUID.fromString(uuID)) + " 님을 찾을 수 없습니다. "));

    }

    @Override
    public List<Channel> findByAll() {
        return data.values().stream().toList();
    }
}
