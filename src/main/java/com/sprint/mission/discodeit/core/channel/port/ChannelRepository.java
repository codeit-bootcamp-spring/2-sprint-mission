package com.sprint.mission.discodeit.core.channel.port;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository {

  Channel save(Channel channel);

  User join(Channel channel, User user);

  User quit(Channel channel, User user);

  Channel find(UUID channelId);

  List<Channel> findAll();

  List<Channel> findAllByChannelId(UUID channelId);

  void remove(UUID channelId);

  boolean existId(UUID id);

  boolean existName(String name);
}
