package com.sprint.mission.discodeit.core.channel.port;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepositoryPort {

  Channel save(Channel channel);

  Optional<Channel> findByChannelId(UUID channelId);

  List<Channel> findAll();

  List<Channel> findAccessibleChannels(ChannelType channelType, List<UUID> subscribedChannelIds);

  void delete(UUID channelId);

  boolean existsById(UUID channelId);
}
