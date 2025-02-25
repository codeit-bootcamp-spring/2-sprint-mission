package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList;

    public JCFChannelService() {
        this.channelList = new ArrayList<>();
    }

    @Override
    public Channel createChannel(Channel channel) {
        boolean channelExists = channelList.stream()
                .anyMatch(existingChannel -> existingChannel.getChannelName().equals(channel.getChannelName()));
        if (channelExists) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다.");
        }
        channelList.add(channel);
        return channel;
    }

    @Override
    public List<Channel> findAllChannel() {
        return channelList;
    }

    @Override
    public Channel findByChannelId(UUID channelId) {
        return channelList.stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 채널을 찾을 수 없습니다: " + channelId));
    }

    @Override
    public Channel updateChannel(UUID channelId, Channel channel) {
        return channelList.stream()
                .filter(existingChannel -> existingChannel.getId().equals(channelId))
                .findFirst()
                .map(existingChannel -> {
                    if (channel.getChannelName() != null) {
                        existingChannel.setChannelName(channel.getChannelName());
                    }
                    return existingChannel;
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 채널을 찾을 수 없습니다: " + channelId));
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelList.removeIf(channel -> channel.getId().equals(channelId));
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userID) {
        Channel channel = findByChannelId(channelId);
        boolean userExists = channel.getChannelUser().stream()
                .anyMatch(existingUserId -> existingUserId.equals(userID));
        if (!userExists) {
            channel.getChannelUser().add(userID);
            updateChannel(channelId, channel);
        } else {
            throw new IllegalArgumentException("사용자가 이미 채널에 참여하고 있습니다.");
        }
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userID) {
        Channel channel = findByChannelId(channelId);
        boolean userExists = channel.getChannelUser().stream()
                .anyMatch(existingUserId -> existingUserId.equals(userID));
        if (userExists) {
            channel.getChannelUser().remove(userID);
            updateChannel(channelId, channel);
        } else {
            throw new IllegalArgumentException("해당 사용자가 채널에 존재하지 않습니다.");
        }
    }

    @Override
    public List<Channel> findByUserId(UUID userID) {
        return channelList.stream()
                .filter(channel -> channel.getChannelUser().contains(userID))
                .collect(Collectors.toList());
    }
}
