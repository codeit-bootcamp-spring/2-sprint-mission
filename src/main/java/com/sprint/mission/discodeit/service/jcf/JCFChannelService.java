package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData;

    public JCFChannelService() {
        this.channelData = new HashMap<>();
    }

    @Override
    public void create(String channelName) {
        Channel channel = new Channel(channelName);
        if(read(channelName) != null){
            System.out.println("이미 존재하는 채널입니다.");
        }
        channelData.put(channel.getId(), channel);
    }

    @Override
    public Channel read(String channelName) {
        return channelData.values().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<UUID, Channel> readAll() {
        return channelData;
    }

    @Override
    public void update(String oldName, String newName) {       //채널명 수정
        Channel channel = read(oldName);
        channel.updateChannel(newName);
    }

    @Override
    public void delete(String channelName) {
        Channel channel = read(channelName);
        if(read(channelName) == null){
            System.out.println("존재하지 않는 채널입니다.");
        }
        channelData.remove(channel.getId());
    }

    @Override
    public String toString() {
        return "JCFChannelService{" +
                "channelData=" + channelData +
                '}';
    }
}
