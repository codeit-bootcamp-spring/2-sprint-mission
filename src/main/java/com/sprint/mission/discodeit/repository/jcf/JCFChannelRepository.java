package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {

    List<Channel> channelList = new ArrayList<>();

    @Override
    public void channelSave(String channelName) {
        Channel channel = new Channel(channelName);
        channelList.add(channel);
        System.out.println("채널 개설 성공" + channel);
    }
}
