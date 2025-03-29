package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


public class BasicMessageRepositoryImplement implements MessageRepository {
    private final Map<UUID, Message> messageRepository = new HashMap<>();

    @Override
    public boolean register(Message message) {
        messageRepository.put(message.getId(), message);
        return true;
    }
    
    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepository.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        return messageRepository.remove(messageId) != null;
    }


    @Override
    public boolean updateMessage(Message message) {
        if (messageRepository.containsKey(message.getId())) {
            messageRepository.put(message.getId(), message);
            return true;
        }
        return false;
    }


    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        
        return messageRepository.values().stream()
            .filter(message -> channelId.equals(message.getChannelId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
        
        return messageRepository.values().stream()
            .filter(message -> authorId.equals(message.getAuthorId()))
            .collect(Collectors.toList());
    }
} 