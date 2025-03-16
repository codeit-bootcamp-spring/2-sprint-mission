package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelRepository {
    void reset();

    UUID join(Channel channel, User user);

    UUID quit(Channel channel, User user);

    UUID save(Server server, Channel channel);

    User findUser(Channel channel, User user);

    Channel findChannel(Server server, Channel channel);

    List<User> findUserListByChannelId(UUID channelId);

    List<Channel> findChannelListByServerId(UUID serverId);

    UUID update(Server server, Channel channel, String replaceName);

    UUID remove(Server server, Channel channel);


}
