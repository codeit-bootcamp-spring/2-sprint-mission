package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
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

    List<Channel> findAllByServerId(UUID serverId);

    Channel update(Channel channel, ChannelCRUDDTO channelUpdateDTO);

    void remove(Server server, Channel channel);
}
