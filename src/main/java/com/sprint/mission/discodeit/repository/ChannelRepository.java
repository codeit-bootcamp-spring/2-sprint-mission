package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.UUID;

public class ChannelRepository {
    private final ArrayDeque<Channel> channels = new ArrayDeque<>();

    private static class SingletonHolder {
        private static final ChannelRepository INSTANCE = new ChannelRepository();
    }

    private ChannelRepository() {}

    public static ChannelRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ArrayDeque<Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel) {
        this.channels.addFirst(channel);
    }

    public Channel findChannelById(UUID channelId) {
        return channels.stream()
                .filter(channel -> Objects.equals(channel.getId(), channelId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 channelId를 가진 channel 을 찾을 수 없다!!!"));
    }

    public void addParticipant(UUID channelId, User newParticipant) {
        findChannelById(channelId).addParticipant(newParticipant);
    }

    public void deleteChannel(UUID channelId) {
        boolean removed = channels.removeIf(channel -> Objects.equals(channel.getId(), channelId));
        if (!removed) {
            throw new IllegalArgumentException("삭제하려는 channelId가 존재하지 않습니다!!!");
        }
    }
}
