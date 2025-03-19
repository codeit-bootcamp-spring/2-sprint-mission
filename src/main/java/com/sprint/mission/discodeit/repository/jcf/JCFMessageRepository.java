package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
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
        if (channelIdMessages.get(channelId) == null) {         // channelService에서 createChannel을 할때 항상 addChannelIdToChannelIdMessage가 호출되어 확인할 필요 없지만 자체적인 검증로직 필요?
            throw new NullPointerException("해당 channelId를 가진 채널이 아직 생성되지 않았습니다. " + channelId);
        }
        return new ArrayList<>(channelIdMessages.get(channelId));
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateContent(newContent);
        }
    }

    @Override
    public void updateAttachmentIds(UUID messageId, List<UUID> attachmentIds) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateAttachmentIds(attachmentIds);
        }
    }

    @Override
    public void deleteAttachment(UUID messageId, UUID attachmentId) {
        if (existsById(messageId)) {
            super.storage.get(messageId).deleteAttachment(attachmentId);
        }
    }

    @Override
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        channelIdMessages.get(super.findById(messageId).getChannelId()).remove(super.findById(messageId));
    }
}
