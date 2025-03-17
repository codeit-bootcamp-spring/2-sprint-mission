package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JCFMessageRepository extends AbstractRepository<Message> implements MessageRepository {
    private static volatile JCFMessageRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장
    private Map<UUID, NavigableSet<Message>> channelIdMessages;

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
    public void addChannelIdToChannelIdMessage(UUID channelId) {
        if (channelIdMessages.containsKey(channelId)) {         // 자체적인 무결성 확보
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channelId);
        }
        channelIdMessages.put(channelId, new TreeSet<>(Comparator.comparing(Message::getCreatedAt)
                .thenComparing(Message::getId)));
    }

    @Override
    public void add(Message newMessage) {
        super.add(newMessage);
        channelIdMessages.computeIfAbsent(newMessage.getChannelId(),
                id -> new TreeSet<>(
                        Comparator.comparing(Message::getCreatedAt)     // 생성된 시간 순서대로 정렬 (오름차순, 오래된 메세지가 첫번째)
                                .thenComparing(Message::getId)          // 생성된 시간이 동일한 경우 id 순으로 정렬 (예외 처리를 위해, 정렬 순서에 별 의미는 없음)
                )
        ).add(newMessage);
    }

    @Override
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        channelIdMessages.get(super.findById(messageId).getChannelId()).remove(super.findById(messageId));
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
