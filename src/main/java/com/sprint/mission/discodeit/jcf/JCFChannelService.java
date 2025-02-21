package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData;
    private static JCFChannelService channelInstance;

    public static JCFChannelService getInstance() {
        if (channelInstance == null) {
            channelInstance = new JCFChannelService();
        }
        return channelInstance;
    }

    private JCFChannelService() {
        this.channelData = new HashMap<>();
    }

    public void containsChannelData(UUID cid){
        if(!(channelData.containsKey(cid))){
            throw new IllegalArgumentException("존재하지 않는 채널ID입니다.");
        }
    }

    @Override
    public Channel getChannel(UUID cid) {
        containsChannelData(cid);
        return channelData.get(cid);
    }

    @Override
    public List<Channel> getAllChannels(){
        if(channelData.isEmpty()){
            throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
        }
        return new ArrayList<>(channelData.values());
    }

    @Override
    public List<Channel> getUpdatedChannels(){
        return channelData.values().stream()
                .filter(entry -> entry.getChannelUpdateAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void registerChannel(String channelName, UUID userId) {
        Channel channel = new Channel(channelName, userId);
        this.channelData.put(channel.getCid(), channel);
    }

    @Override
    public void updateChannel(UUID cid, String channelName) {
        containsChannelData(cid);
        channelData.get(cid).channelUpdate(channelName);
    }


    public void deleteChannel(UUID cid) {
        containsChannelData(cid);
        channelData.remove(cid);
    }
}
