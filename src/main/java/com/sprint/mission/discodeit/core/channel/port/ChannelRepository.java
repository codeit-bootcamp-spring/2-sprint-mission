package com.sprint.mission.discodeit.core.channel.port;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository {

  Channel save(Channel channel);

  Optional<Channel> findByChannelId(UUID channelId);

  List<Channel> findAll();

  List<Channel> findAllByChannelId(UUID channelId);

  void remove(UUID channelId);

//  User join(Channel channel, User user);
//
//  User quit(Channel channel, User user);
}
