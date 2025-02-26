package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;


public class ChannelEntity extends BaseEntity {
    private String name;
    private String type;

    public ChannelEntity(String channelName, String channelType) {
        super();
        this.name = channelName;
        this.type = channelType;
    }

    public String getName() { return name;}
    public String getType() { return type;}

    public void updateChannelName(String newName) {
        this.name = newName;
    }

    public void updateChannelType(String newType) {
        if (!newType.equals("text")&& !newType.equals("voice")) {
            throw new IllegalArgumentException("유효한 채널 유형이 아닙니다.");
        }
        this.type = newType;
    }

    public static void addChannel(List<ChannelEntity> channelList, ChannelEntity newChannel) {
        if (!channelList.contains(newChannel)) {
            channelList.add(newChannel);
        }
    }

    public static void deleteChannel(List<ChannelEntity> channelList, ChannelEntity channel) {
        channelList.remove(channel);
    }
}
