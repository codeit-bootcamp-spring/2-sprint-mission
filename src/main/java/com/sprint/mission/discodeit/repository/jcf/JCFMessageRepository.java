package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private static volatile JCFMessageRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장
    private final HashMap<UUID, Message> messages;

    private JCFMessageRepository() {
        messages = new LinkedHashMap<>();
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
    public void add(Message newMessage) {
        if (newMessage == null) {
            throw new IllegalArgumentException("input newMessage is null!!!");
        }
        messages.put(newMessage.getId(), newMessage);
    }

    @Override
    public boolean existsById(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("null값을 가지는 messageId가 들어왔습니다!!!");
        }
        return messages.containsKey(messageId);
    }

    @Override
    public Message findById(UUID messageId) {
        if (!existsById(messageId)) {
            throw new NoSuchElementException("해당 messageId를 가진 메세지를 찾을 수 없습니다 : " + messageId);
        }
        return messages.get(messageId);
    }

    @Override
    public HashMap<UUID, Message> getAll() {
        return messages;
    }

    @Override
    public void deleteById(UUID messageId) {
        if (!existsById(messageId)) {
            throw new NoSuchElementException("해당 channelId를 가진 메세지를 찾을 수 없습니다 : " + messageId);
        }
        messages.remove(messageId);
    }

    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        return messages.values().stream()
                .filter((m) -> Objects.equals(channelId, m.getChannel().getId()))
                .collect(Collectors.toList());
    }
}
