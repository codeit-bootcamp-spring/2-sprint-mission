package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private volatile static JCFChannelService instance = null;
    private final Map<UUID, Channel> channelRepository;

    public JCFChannelService() {
        this.channelRepository = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFChannelService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    @Override
    public Channel saveChannel(String name) {
        Channel channel = new Channel(name);
        channelRepository.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        if(channelRepository.isEmpty()){
            throw new NoSuchElementException("채널이 없습니다.");
        }
        return channelRepository.values().stream().toList();
    }

    @Override
    public List<Channel> findByName(String name) {
        List<Channel> channels = channelRepository.values().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .toList();

        if(channels.isEmpty()){
            throw new NoSuchElementException("해당 이름의 채널이 없습니다.");
        }
        return channels;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelRepository.get(id));
    }

    @Override
    public void update(UUID id, String name) {
        if(!channelRepository.containsKey(id)){
            throw new NoSuchElementException("채널이 없습니다.");
        }
        if(name == null){
            throw new IllegalArgumentException("수정할 이름은 null일 수 없습니다.");
        }

        channelRepository.get(id).setName(name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.remove(id);
    }
}