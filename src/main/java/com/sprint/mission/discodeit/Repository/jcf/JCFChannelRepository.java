package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public class JCFChannelRepository implements ChannelRepository {
    private List<Message> list;

    public void save(Message message) {
        list.add(message);
    }

    public void updateMessageList(List<Message> list) {
        this.list = list;
    }

    public List<Message> getList() {
        return list;
    }

}
