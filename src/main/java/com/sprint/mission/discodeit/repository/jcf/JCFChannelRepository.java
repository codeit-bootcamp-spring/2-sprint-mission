package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static volatile JCFChannelRepository instance;

    private final Map<UUID, Channel> channels;
    private final Map<UUID, Set<UUID>> memberIdsByChannelId;

    private JCFChannelRepository() {
        channels = new HashMap<>();
        memberIdsByChannelId = new HashMap<>();
    }

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            synchronized (JCFChannelRepository.class) {
                if (instance == null) {
                    instance = new JCFChannelRepository();
                }
            }
        }
        return instance;
    }


    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        memberIdsByChannelId.putIfAbsent(channel.getId(), new HashSet<>());
        memberIdsByChannelId.get(channel.getId()).add(channel.getOwnerId());
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public Set<UUID> findMemberIds(UUID channelId) {
        return memberIdsByChannelId.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void delete(UUID channelId) {
        channels.remove(channelId);
        memberIdsByChannelId.remove(channelId);
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        memberIdsByChannelId.get(channelId).add(userId);
    }

    @Override
    public void removeMember(UUID channelId, UUID userId) {
        memberIdsByChannelId.get(channelId).remove(userId);
    }

    @Override
    public boolean exists(UUID channelId) {
        return channels.containsKey(channelId) && channels.containsKey(channelId);
    }
}
