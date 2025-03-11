package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelRepository extends AbstractRepository<Channel> implements ChannelRepository {
    private static volatile JCFChannelRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private JCFChannelRepository() {
        super(Channel.class, new ConcurrentHashMap<>());
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
    public void updateChannelName(UUID channelId, String newChannelName) {
        if (existsById(channelId)) {
            super.storage.get(channelId).updateChannelName(newChannelName);
        }
    }

    @Override
    public void addParticipant(UUID channelId, UUID newParticipantId) {
        super.findById(channelId).addParticipant(newParticipantId);
    }
}
