package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    void create(Channel message);
    Channel findByChannelId(UUID channelId);
    List<Channel> findAll();
    void modify(UUID channelId, String name); // 채널의 이름 변경
    Channel remove(UUID channelId);
}
