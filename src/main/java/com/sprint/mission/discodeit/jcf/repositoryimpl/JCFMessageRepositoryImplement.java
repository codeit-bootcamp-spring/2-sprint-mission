package com.sprint.mission.discodeit.jcf.repositoryimpl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;

import java.util.*;

public class JCFMessageRepositoryImplement implements MessageRepository {
    private final Map<UUID, Message> messageRepository;

    public JCFMessageRepositoryImplement() {
        this.messageRepository = new HashMap<>();
    }

    @Override
    public void register(Message message) {
        messageRepository.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepository.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        return messageRepository.remove(messageId) != null;
    }
} 