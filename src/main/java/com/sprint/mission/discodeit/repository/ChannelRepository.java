package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class ChannelRepository {
    private final Map<UUID, Channel> channels = new HashMap<>();

    private static class SingletonHolder {
        private static final ChannelRepository INSTANCE = new ChannelRepository();
    }

    private ChannelRepository() {}

    public static ChannelRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Map<UUID, Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel) {
        this.channels.put(channel.getId(), channel);
    }

    public boolean existsChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("null값을 가지는 channelId가 들어왔습니다!!!");
        }
        return channels.containsKey(channelId);
    }

    public Channel findChannelById(UUID channelId) {
        if (!existsChannel(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
        return channels.get(channelId);
    }

    public void addParticipant(UUID channelId, User newParticipant) {
        findChannelById(channelId).addParticipant(newParticipant);
    }

    public void deleteChannel(UUID channelId) {
        if (!existsChannel(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
        channels.remove(channelId);
    }
}
