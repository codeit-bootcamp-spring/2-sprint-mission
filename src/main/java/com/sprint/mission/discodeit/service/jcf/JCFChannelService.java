package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;

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
        Channel channel = new Channel(ownerId, title, description);
        channels.put(channel.getId(), channel);

        Set<UUID> members = new HashSet<>();
        members.add(ownerId);

        memberIdsByChannelId.put(channel.getId(), members);

        return channel;
    }

    @Override
    public UUID getChannelOwnerId(UUID channelId) {
        Channel channel = channels.get(channelId);
        if (channel == null) return null;

        return channel.getOwnerId();
    }

    @Override
    public Channel getChannelByChannelId(UUID channelId) {
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
        return memberIdsByChannelId.get(channelId);
    }

    @Override
    public Channel updateChannel(UUID channelId, UUID ownerId, String title, String description) {
        Channel channel = channels.get(channelId);
        channel.update(channelId, title, description);

        return channel;
    }

    @Override
    public boolean deleteChannelByChannelId(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            return false;
        }

        channels.remove(channelId);
        memberIdsByChannelId.remove(channelId);

        return true;
    }

    @Override
    public Channel addUserToChannel(UUID channelId, UUID userId) {
        memberIdsByChannelId.get(channelId).add(userId);
        return getChannelByChannelId(channelId);
    }

    @Override
    public Channel deleteUserFromChannel(UUID channelId, UUID userId) {
        if (userId.equals(getChannelOwnerId(channelId))) {
            return getChannelByChannelId(channelId);
        }

        memberIdsByChannelId.get(channelId).remove(userId);
        return getChannelByChannelId(channelId);
    }

    protected boolean isChannelMember(UUID channelId, UUID userId) {
        Set<UUID> channelMembers = memberIdsByChannelId.get(channelId);
        if (channelMembers == null) {
            return false;
        }

        return channelMembers.stream()
                .anyMatch(memberId -> memberId.equals(userId));
    }
}
