package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final HashMap<UUID, Channel> channels = new HashMap<>();
    private static JCFChannelRepository channelRepository;


    private JCFChannelRepository(){}

    public static JCFChannelRepository getInstance(){
        if(channelRepository == null){
            channelRepository = new JCFChannelRepository();
        }

        return channelRepository;
    }


    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public Optional<List<Channel>> findAll() {
        return Optional.of(new ArrayList<>(channels.values()));
    }

    @Override
    public void update(Channel channel) {
       save(channel);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
