package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private volatile static BasicChannelService instance = null;
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            synchronized (BasicChannelService.class) {
                if (instance == null) {
                    instance = new BasicChannelService(channelRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public Channel saveChannel(String name) {
        Channel channel = new Channel(name);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        if(channelRepository.findAll().isEmpty()){
            throw new NoSuchElementException("등록된 채널이 없습니다.");
        }

        return channelRepository.findAll();
    }

    @Override
    public List<Channel> findByName(String name) {
        List<Channel> channels = channelRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .toList();
        if(channels.isEmpty()){
            throw new NoSuchElementException("등록된 채널이 없습니다. : " + name);
        }
        return channels;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        try{
            return Optional.ofNullable(channelRepository.findById(id));
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("등록된 채널이 없습니다. : " + id);
        }
    }

    @Override
    public void update(UUID id, String name) {
        try{
            Channel channel = channelRepository.findById(id);
            channel.setName(name);
            channelRepository.save(channel);
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("업데이트 할 유저가 없습니다.");
        }catch(NullPointerException e){
            throw new NullPointerException("수정할 이름은 null일 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
