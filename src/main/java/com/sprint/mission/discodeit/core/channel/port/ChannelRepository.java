package com.sprint.mission.discodeit.core.channel.port;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelRepository {

  void reset();

  Channel save(Server server, Channel channel);

  User join(Channel channel, User user);

  User quit(Channel channel, User user);

  Channel find(UUID channelId);

  List<Channel> findAll();

  List<Channel> findAllByChannelId(UUID channelId);

  List<Channel> findAllByServerId(UUID serverId);

  Channel update(Channel channel, UpdateChannelDTO updateChannelDTO);

  void remove(UUID channelId);

  boolean existId(UUID id);

  boolean existName(String name);
}
