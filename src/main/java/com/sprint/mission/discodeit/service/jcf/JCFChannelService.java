package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static JCFChannelService INSTANCE;
    private final Map<UUID, Channel> channels = new HashMap<>();
    private final UserService userService;

    private JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    public static synchronized JCFChannelService getInstance(UserService userService) {
        if (INSTANCE == null) {
            INSTANCE = new JCFChannelService(userService);
        }
        return INSTANCE;
    }

    @Override
    public void updataChannelData() {

    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        return channel;
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
        channel.updateChannelName(newChannelName);
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);

        if (channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        userService.addChannel(userId, channelId);
        channel.addMembers(userId);
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
            userService.deleteChannel(userId, channelId);
        }

        channels.remove(channelId);
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("채널에 존재하지 않는 유저입니다.");
        }

        userService.deleteChannel(userId, channelId);
        channel.removeMember(userId);
    }

    @Override
    public void removeMessageFromChannel(UUID channelId, UUID messageId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isMessageInChannel(messageId)) {
            throw new IllegalArgumentException("체널에 존재하지 않는 메세지 입니다.");
        }

        channel.removeMessage(messageId);
    }

    public void validateChannelExists(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
