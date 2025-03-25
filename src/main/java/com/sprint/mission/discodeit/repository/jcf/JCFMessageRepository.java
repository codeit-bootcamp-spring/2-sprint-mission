package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public UUID createMessage(Message message) {
        data.put(message.getId(), message);
        return findById(message.getId()).getId();
    }

    @Override
    public Message findById(UUID id) {
        Message message = data.get(id);
        if(message == null){
            throw new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id);
        }
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        checkMessageExists(id);
        Message message = data.get(id);

        message.update(content, attachmentIds);
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        checkMessageExists(id);

        data.remove(id);
    }

    @Override
    public Optional<Instant> findLatestCreatedAtByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder());
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        Message message = data.values().stream()
                .filter(result -> result.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 채널의 메시지를 찾을 수 없습니다: " + channelId));

        data.remove(message.getId());
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkMessageExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id);
        }
    }

}
