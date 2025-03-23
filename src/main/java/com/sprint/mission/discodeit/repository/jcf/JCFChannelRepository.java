package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public UUID createChannel(Channel channel) {
        data.put(channel.getId(), channel);
        return channel.getId();
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
    public Channel findById(UUID id) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id);
        }
        return channel;
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
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        checkChannelExists(id);
        Channel channel = data.get(id);

        channel.update(name, category, type);
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        checkChannelExists(id);

        data.remove(id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkChannelExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id);
        }
    }

}
