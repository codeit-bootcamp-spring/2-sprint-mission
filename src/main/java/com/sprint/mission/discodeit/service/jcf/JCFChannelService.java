package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService{
    private final Map<UUID, Channel> channelList;

    public JCFChannelService(){
        this.channelList = new HashMap<>();
    }

    @Override
    public Channel createChannel(ChannelType type, String channelName, String description){
        Channel channel = new Channel(type, channelName, description);
        this.channelList.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public List<Channel> findAllChannel(){
        return new ArrayList<>(channelList.values());
    }

    @Override
    public Channel findByChannelId(UUID channelId){
        Channel channelNullable = this.channelList.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
    }

    @Override
    public Channel updateChannel(UUID channelId, String newChannelName, String newDescription){
        Channel channelNullable = this.channelList.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
        channel.update(newChannelName, newDescription);

        return channel;
        }

    @Override
    public void deleteChannel(UUID channelId){
        Channel removedChannel = this.channelList.remove(channelId);
        if(removedChannel == null) {
            throw new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId);
        }
    }
}
