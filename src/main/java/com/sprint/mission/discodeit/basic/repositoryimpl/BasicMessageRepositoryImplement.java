package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;

import java.util.*;

public class BasicMessageRepositoryImplement implements MessageRepository {
    private final Map<UUID, Message> messageRepository;
    
    // 싱글톤 인스턴스
    private static BasicMessageRepositoryImplement instance;
    
    // private 생성자로 변경
    private BasicMessageRepositoryImplement() {
        this.messageRepository = new HashMap<>();
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized BasicMessageRepositoryImplement getInstance() {
        if (instance == null) {
            instance = new BasicMessageRepositoryImplement();
        }
        return instance;
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

    @Override
    public boolean updateMessage(Message message) {
        return messageRepository.put(message.getId(), message) != null;
    }
} 