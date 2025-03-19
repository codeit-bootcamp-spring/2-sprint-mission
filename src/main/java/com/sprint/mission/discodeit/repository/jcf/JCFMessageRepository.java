package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFMessageRepository extends AbstractRepository<Message> implements MessageRepository {
    private Map<UUID, NavigableSet<Message>> channelIdMessages;

    private JCFMessageRepository() {
        super(Message.class, new ConcurrentHashMap<>());
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
        channelIdMessages.get(newMessage.getChannelId()).add(newMessage);
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

    @Override
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        channelIdMessages.get(super.findById(messageId).getChannelId()).remove(super.findById(messageId));
    }
}
