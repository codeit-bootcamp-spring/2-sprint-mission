package com.sprint.mission.discodeit.core.channel.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface CustomChannelRepository {

  Channel findByChannelId(UUID id);

  List<Channel> findAllByTypeOrIdIn(ChannelType type, List<UUID> ids);

}
