package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static volatile JCFChannelRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장
    private final Map<UUID, Channel> channels;

    private JCFChannelRepository() {
        channels = new HashMap<>();
    }

    public static JCFChannelRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (JCFChannelRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new JCFChannelRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(Channel channel) {
        this.channels.put(channel.getId(), channel);
    }

    @Override
    public boolean existsById(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("null값을 가지는 channelId가 들어왔습니다!!!");
        }
        return channels.containsKey(channelId);
    }

    @Override
    public Channel findById(UUID channelId) {
        if (!existsById(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
        return channels.get(channelId);
    }

    @Override
    public Map<UUID, Channel> getAll() {
        return channels;
    }

    @Override
    public void deleteById(UUID channelId) {
        if (!existsById(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 채널이 존재하지 않습니다 : " + channelId);
        }
        channels.remove(channelId);
    }

    public void addParticipant(UUID channelId, User newParticipant) {
        findById(channelId).addParticipant(newParticipant);
    }
}
