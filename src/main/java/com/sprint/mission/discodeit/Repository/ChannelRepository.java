package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface ChannelRepository {
    void setList(List<Message> list);

    List<Message> getList();

    void add(Message message);
}
