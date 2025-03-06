package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public class JCFServerRepository implements ServerRepository  {
    private List<Channel> list;

    public void save(Channel channel) {
        list.add(channel);
    }

    public List<Channel> getContainerList() {
        return list;
    }

    @Override
    public void updateContainerList(List<Channel> channelList) {
        this.list = channelList;
    }
}
