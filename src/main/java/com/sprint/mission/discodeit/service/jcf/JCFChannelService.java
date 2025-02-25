package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;

    private UserService userService;
    private final Map<UUID, Channel> channels;
    private final Map<UUID, Set<UUID>> memberIdsByChannelId;

    private JCFChannelService() {
        channels = new HashMap<>();
        memberIdsByChannelId = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }

        return instance;
    }

    @Override
    public Channel createChannel(UUID ownerId, String title, String description) {
        getUserService().validateUserId(ownerId);
        Channel channel = new Channel(ownerId, title, description);
        channels.put(channel.getId(), channel);

        Set<UUID> members = new HashSet<>();
        members.add(ownerId);

        memberIdsByChannelId.put(channel.getId(), members);

        return channel;
    }

    @Override
    public UUID getChannelOwnerId(UUID channelId) {
        return getChannelByChannelId(channelId).getOwnerId();
    }

    @Override
    public Channel getChannelByChannelId(UUID channelId) {
        validateChannelId(channelId);
        return channels.get(channelId);
    }

    @Override
    public List<Channel> getChannelsByTitle(String title) {
        return channels.values().stream()
                .filter(c -> title.equals(c.getTitle()))
                .toList();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channels.values().stream().toList();
    }

    @Override
    public Set<UUID> getChannelMembers(UUID channelId) {
        validateChannelId(channelId);
        return memberIdsByChannelId.get(channelId);
    }

    @Override
    public Channel updateChannel(UUID channelId, String title, String description) {
        validateChannelId(channelId);
        Channel channel = getChannelByChannelId(channelId);
        channel.update(channelId, title, description);
        return channel;
    }

    @Override
    public void deleteChannelByChannelId(UUID channelId) {
        validateChannelId(channelId);
        channels.remove(channelId);
        memberIdsByChannelId.remove(channelId);
    }

    @Override
    public Channel addUserToChannel(UUID channelId, UUID userId) {
        getUserService().validateUserId(userId);
        validateChannelId(channelId);
        memberIdsByChannelId.get(channelId).add(userId);
        return getChannelByChannelId(channelId);
    }

    @Override
    public void deleteUserFromChannel(UUID channelId, UUID userId) {
        getUserService().validateUserId(userId);
        validateChannelId(channelId);
        if (userId.equals(getChannelOwnerId(channelId))) {
            throw new IllegalArgumentException("채널 소유자 삭제 불가");
        }

        memberIdsByChannelId.get(channelId).remove(userId);
    }

    @Override
    public void deleteUserFromEveryChannel(UUID userId) {
        if (isOwnerOfAnyChannel(userId)) {
            throw new IllegalArgumentException("채널 소유자 삭제 불가");
        }

        channels.keySet().stream().forEach(channelId ->
                deleteUserFromChannel(channelId, userId));
    }

    @Override
    public boolean isChannelMember(UUID channelId, UUID userId) {
        validateChannelId(channelId);
        Set<UUID> channelMembers = memberIdsByChannelId.get(channelId);

        return channelMembers.stream()
                .anyMatch(memberId -> memberId.equals(userId));
    }

    @Override
    public void validateChannelId(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new ChannelNotFoundException("해당 채널 없음");
        }
        if (!memberIdsByChannelId.containsKey(channelId)) {
            throw new ChannelNotFoundException("해당 채널 없음");
        }
    }

    private boolean isOwnerOfAnyChannel(UUID userId) {
        getUserService().validateUserId(userId);
        return channels.values().stream()
                .anyMatch(channel -> userId.equals(channel.getOwnerId()));
    }

    private UserService getUserService() {
        if (userService == null) {
            userService = JCFUserService.getInstance();
        }
        return userService;
    }
}

