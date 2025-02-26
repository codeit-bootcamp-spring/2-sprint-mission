package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();
    private final Map<String, UUID> channelIds = new HashMap<>();
    private final UserService userService;
    private static JCFChannelService INSTANCE;

    private JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    public static synchronized JCFChannelService getInstance(UserService userService){
        if(INSTANCE == null){
            INSTANCE = new JCFChannelService(userService);
        }
        return INSTANCE;
    }

    @Override
    public void createChannel(String channelName) {
        if(channelIds.containsKey(channelName)){
            throw new IllegalArgumentException("이미 존재하는 채널입니다.");
        }

        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        channelIds.put(channelName, channel.getId());
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId);
    }

    @Override
    public String getChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = getChannelById(channelId);

        // 채널명이 이미 존재하는지 확인
        for (Channel existingChannel : channels.values()) {
            if (existingChannel.getChannelName().equals(newChannelName)) {
                throw new IllegalArgumentException("이미 존재하는 채널명입니다.");
            }
        }

        String oldChannelName = channel.getChannelName();
        channel.updateChannelName(newChannelName);
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        User user = userService.getUserById(userId);

        if (channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        channel.addMembers(userId);
        user.addJoinedChannel(channelId);
    }

    @Override
    public void addMessageToChannel(UUID channelId, UUID messageId) {
        Channel channel = getChannelById(channelId);

        channel.addMessages(messageId);
    }

    @Override
    public void removeChannel(UUID channelId) {
        Channel channel = getChannelById(channelId);

        for (UUID userId : channel.getMembers()) {
            User user = userService.getUserById(userId);
            if (user != null) {
                user.removeJoinedChannel(channelId);
            }
        }

        channels.remove(channelId);
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("가입되지 않는 유저입니다.");
        }

        User user = userService.getUserById(userId);
        user.removeJoinedChannel(channelId);
        channel.removeMember(userId);
    }

    @Override
    public void removeMessageFromChannel(UUID channelId, UUID messageId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isMessageInChannel(messageId)) {
            throw new IllegalArgumentException("존재하지 않는 메세지 입니다.");
        }

        channel.removeMessage(messageId);
    }

    public void validateChannelExists(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
