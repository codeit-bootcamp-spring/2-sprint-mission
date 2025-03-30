package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.*;

public class MessageRepositoryImpl implements MessageRepository {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public Message save(Message message) {
        messages.add(message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id){
        return messages.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId){
        return messages.stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public boolean existsById(UUID id){
        return messages.stream().anyMatch(m -> m.getId().equals(id));
    }

    @Override
    public void deleteByMessageId(UUID id) {
        messages.removeIf(m -> m.getId().equals(id));
    }

    @Override
    public void deleteByChannelId(UUID channelId){
        messages.removeIf(m -> m.getChannelId().equals(channelId));
    }

    @Override
    public Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId){
        return messages.stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .map( m-> m.getCreatedAtSeconds())
                .max((time1, time2) -> time1.compareTo(time2));
    }


}
