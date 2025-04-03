package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity._Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, _Channel> data;

  public JCFChannelRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public _Channel save(_Channel channel) {
    this.data.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<_Channel> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<_Channel> findAll() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }
}
