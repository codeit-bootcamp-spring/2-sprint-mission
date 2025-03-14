package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {

    void save(Channel channel);
    List<Channel> load();
    void remove(Channel channel);

}
