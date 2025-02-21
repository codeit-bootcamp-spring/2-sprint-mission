package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService{

    private static JCFChannelService jcfChannel;

    HashMap<UUID, Channel> channels = new HashMap<>();

    private JCFChannelService(){}

    public static JCFChannelService getInstance(){
        if(jcfChannel == null){
            jcfChannel = new JCFChannelService();
        }

        return jcfChannel;
    }

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void update(UUID id) {
        Optional<Channel> channel = this.findById(id);

        if (channel.isEmpty()) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }

        channel.ifPresent(ch -> ch.setUpdatedAt(System.currentTimeMillis()));
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}

