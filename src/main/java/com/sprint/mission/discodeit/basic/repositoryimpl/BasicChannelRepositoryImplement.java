package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//asdasdasdsdadasd
public class BasicChannelRepositoryImplement implements ChannelRepository {

  // 채널 id/채널
  private final Map<UUID, Channel> channelRepository = new HashMap<>();

  @Override
  public boolean register(Channel channel) {
    channelRepository.put(channel.getChannelId(), channel);
    return true;
  }

  @Override
  public Set<UUID> allChannelIdList() {
    return new HashSet<>(channelRepository.keySet());
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    return Optional.ofNullable(channelRepository.get(channelId));
  }

  @Override
  public Optional<String> findChannelNameById(UUID channelId) {
    Channel channel = channelRepository.get(channelId);
    return channel != null ? Optional.of(channel.getName()) : Optional.empty();
  }

  @Override
  public Optional<Channel> findByName(String name) {
    return channelRepository.values().stream()
        .filter(channel -> channel.getName().equals(name))
        .findFirst();
  }

  @Override
  public boolean deleteChannel(UUID channelId) {
    return channelRepository.remove(channelId) != null;
  }

  @Override
  public boolean updateChannel(Channel channel) {
    if (channelRepository.containsKey(channel.getChannelId())) {
      channelRepository.put(channel.getChannelId(), channel);
      return true;
    }
    return false;
  }
}