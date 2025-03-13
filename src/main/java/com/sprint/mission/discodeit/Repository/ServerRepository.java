package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ServerRepository {

    UUID saveUser(Channel channel, User user);

    UUID saveChannel(Server server, Channel channel);

    User findUser(Channel channel, User user);

    Channel findChannel(Server server, Channel channel);

    Channel findChannelByChanelId(Server server, UUID channelId);

    Channel findChannelByChanelId(UUID serverId, Channel channel);

    Channel findChannelByChanelId(UUID serverId, UUID channelId);

    List<User> findUserListByChannelId(UUID channelId);

    List<Channel> findChannelListByServerId(UUID serverId);

    UUID updateChannelName(Server server, Channel channel, String replaceName);

    UUID removeChannel(Server server, Channel channel);

    UUID quitChannel(Channel channel, User user);


}
