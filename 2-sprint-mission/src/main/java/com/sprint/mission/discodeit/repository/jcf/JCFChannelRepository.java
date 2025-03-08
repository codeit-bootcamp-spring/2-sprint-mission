package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        Channel channel = data.get(id);
        userMembers.forEach(channel::addMember);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        Channel channel = data.get(id);
        userMembers.forEach(channel::removeMember);
    }

    @Override
    public Optional<Channel> selectChannelById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> selectAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        Channel channel = data.get(id);
        channel.update(name, category, type);
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        data.remove(id);
    }
}
