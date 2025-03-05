package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface ChannelRepository {
    void save(Message message);

    void updateMessageList(List<Message> list);

    List<Message> getList();

}
