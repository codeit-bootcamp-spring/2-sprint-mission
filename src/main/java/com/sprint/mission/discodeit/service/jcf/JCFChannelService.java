package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        data.put(channel.getId(), channel);
        System.out.println("채널이 생성되었습니다: \n" + channel);
        return channel;
    }

    @Override
    public Channel searchChannel(UUID channelId) {
        Channel channel = findChannel(channelId);
        System.out.println("CHANNEL: " + channel);
        return channel;
    }


    @Override
    public List<Channel> searchAllChannels() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("등록된 채널이 존재하지 않습니다.");
        }
        List<Channel> channels = new ArrayList<>(data.values());
        for (Channel channel : channels) {
            System.out.println("CHANNEL: " + channel);
        }
        return channels;
    }

    @Override
    public Channel updateAll(UUID channelId, String channelName, String description) {
        Channel channel = findChannel(channelId);
        channel.updateAll(channelName, description);
        System.out.println(channelId + " 채널 업데이트 완료되었습니다.");
        return channel;
    }

    @Override
    public Channel updateChannelName(UUID channelId, String channelName) {
        Channel channel = findChannel(channelId);
        channel.updateChannelName(channelName);
        System.out.println(channelId + " 채널 이름이 업데이트 완료되었습니다.");
        return channel;

    }

    @Override
    public Channel updateChannelDescription(UUID channelId, String description) {
        Channel channel = findChannel(channelId);
        channel.updateChannelDescription(description);
        System.out.println(channelId + " 채널 설명이 업데이트 완료되었습니다.");
        return channel;
    }

    @Override
    public void deleteChannel(UUID channelId) {
        findChannel(channelId);
        data.remove(channelId);
        System.out.println(channelId + " 채널 삭제 완료되었습니다.");
    }

    public Channel findChannel(UUID channelId) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new NoSuchElementException("해당 채널이 존재하지 않습니다.");
        }
        return channel;
    }
}