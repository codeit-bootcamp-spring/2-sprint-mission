package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ServerRepository {
    void save(Channel channel);

    List<Channel> getContainerList();

    void updateContainerList(List<Channel> channelList);

}
