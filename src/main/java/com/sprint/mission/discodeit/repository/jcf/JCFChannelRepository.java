package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JCFChannelRepository implements ChannelRepository {

    private static final Map<UUID, Channel> channelDb = new HashMap<>();

    private static void validChannelId(UUID channelId) {
        if (!channelDb.containsKey(channelId)) {
            throw new NoSuchElementException("[ERROR]Channel ID Error");
        }
    }

    @Override
    public void save(Channel channel) {
        channelDb.put(channel.getId(), channel);
    }

    @Override
    public Channel findByChannelId(UUID channelId) {
        return Optional.ofNullable(channelDb.get(channelId))
            .orElseThrow(
                () -> new IllegalArgumentException("[ERROR]유효하지 않은 아이디 입니다. id : " + channelId));
    }

    @Override
    public List<Channel> findAll() {
        return channelDb.values().stream()
            .sorted(Comparator.comparing(Channel::getCreatedAt)).toList();
    }

    @Override
    public void delete(UUID channelId) {
        validChannelId(channelId);
        channelDb.remove(channelId);
    }

    @Override
    public boolean existById(UUID channelId) {
        return channelDb.containsKey(channelId);
    }
}
