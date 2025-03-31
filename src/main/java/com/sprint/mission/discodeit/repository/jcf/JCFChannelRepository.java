package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Channel> findByType(ChannelType type) {
        return data.values().stream()
                .filter(channel -> channel.getType().equals(type))
                .toList();
    }

    @Override
    public List<Channel> findByUserIdAndType(UUID userId, ChannelType type) {
        return data.values().stream()
                .filter(channel -> channel.getUserId().equals(userId) && channel.getType().equals(type))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

//    @Override
//    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
//        Channel channel = data.get(id);
//        userMembers.forEach(channel::addMember);
//    }
//
//    @Override
//    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
//        Channel channel = data.get(id);
//        userMembers.forEach(channel::removeMember);
//    }


}
