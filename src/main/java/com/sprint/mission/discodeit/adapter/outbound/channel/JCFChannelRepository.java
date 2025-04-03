package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> channelList = new ConcurrentHashMap<>();

  @Override
  public Channel save(Channel channel) {
    channelList.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findByChannelId(UUID channelId) {
    return Optional.ofNullable(channelList.get(channelId));
  }

  @Override
  public List<Channel> findAll() {
    return channelList.values().stream().toList();
  }

  @Override
  public List<Channel> findAllByChannelId(UUID channelId) {
    return channelList.values().stream().filter(channel -> channel.getId().equals(channelId))
        .toList();
  }

  @Override
  public void delete(UUID channelId) {
    channelList.remove(channelId);
  }

  @Override
  public boolean existId(UUID channelId) {
    if (channelId == null) {
      return false;
    }
    return channelList.containsKey(channelId);
  }
}
