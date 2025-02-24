package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    public void findAll() {
        channelRepository.values().stream()
                .findFirst()
                .ifPresentOrElse(
                        channel -> channelRepository.values().forEach(System.out::println),
                        () -> System.out.println("등록된 채널이 없습니다.")
                );
    }

    @Override
    public void findByName(String name) {
        channelRepository.values().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println(name + "으로 등록된 채널이 없습니다.")
                );
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelRepository.get(id));
    }

    @Override
    public void update(UUID id, String name) {
        Optional<Channel> channel = Optional.ofNullable(channelRepository.get(id));

        channel.ifPresentOrElse(
                ch -> ch.setName(name),
                () -> System.out.println("해당 id로 등록된 채널이 없습니다.")
        );
    }

    @Override
    public void delete(UUID id) {
        channelRepository.remove(id);
    }
}