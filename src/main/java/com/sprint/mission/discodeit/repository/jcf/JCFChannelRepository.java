package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }
    @Override
    public Channel save(Channel channel){
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel update(Channel channel, String newName, String newDescription){
        channel.update(newName, newDescription);

        return channel;
    }

    @Override
    public List<Channel> findAll(){
        return this.data.values().stream().toList();
    }

    @Override
    public Channel findById(UUID channelId){
        return Optional.ofNullable(data.get(channelId)).orElseThrow(()->new NoSuchElementException("channel with id " + channelId + " not found"));
    }

    @Override
    public Map<UUID, Channel> getChannelData(){
        return this.data;
    }

    @Override
    public void delete(UUID channelId){
        data.remove(channelId);
    }
}
