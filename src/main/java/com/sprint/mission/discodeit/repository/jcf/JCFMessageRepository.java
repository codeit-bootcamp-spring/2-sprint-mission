package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JCFMessageRepository extends AbstractRepository<Message> implements MessageRepository {
    private static volatile JCFMessageRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private JCFMessageRepository() {
        super(Message.class, new ConcurrentHashMap<>());
    }

    public static JCFMessageRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (JCFMessageRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new JCFMessageRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelId == null) {
            throw new NullPointerException("channelId is null");
        }
        return super.storage.values().stream()
                .filter((m) -> Objects.equals(channelId, m.getChannelId()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateContent(newContent);
        }
    }
}
