package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryPortAdapter implements ChannelRepositoryPort {

  private final JpaChannelRepository jpaChannelRepository;

  @Override
  public Channel save(Channel channel) {
    return jpaChannelRepository.save(channel);
  }

  @Override
  public Optional<Channel> findByChannelId(UUID channelId) {
    return jpaChannelRepository.findById(channelId);
  }

  @Override
  public List<Channel> findAll() {
    return jpaChannelRepository.findAll();
  }

  @Override
  public List<Channel> findAccessibleChannels(ChannelType channelType,
      List<UUID> subscribedChannelIds) {
    if (subscribedChannelIds.isEmpty()) {
      return jpaChannelRepository.findAllByType(channelType);
    } else {
      return jpaChannelRepository.findAccessibleChannels(channelType, subscribedChannelIds);
    }
  }

  @Override
  public void delete(UUID channelId) {
    jpaChannelRepository.deleteById(channelId);
  }

  @Override
  public boolean existsById(UUID channelId) {
    return jpaChannelRepository.existsById(channelId);
  }

}
