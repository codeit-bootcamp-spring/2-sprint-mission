package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelService implements ChannelService {
    // 싱글톤 패턴 적용
    private static volatile JCFChannelService instance;
    private final Map<UUID, Channel> data = new ConcurrentHashMap<>();

    public JCFChannelService(){}

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
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Channel entity) {
        if (data.containsKey(id)) {
            entity.update();
            data.put(id, entity);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
