package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataMessageRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jpa", matchIfMissing = true)
@Repository
@RequiredArgsConstructor
public class JpaMessageRepository implements MessageRepository {
    private final SpringDataMessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannel_Id(channelId);
    }

    @Override
    public Page<Message> findAllByChannelIdPaging(UUID channelId, Pageable pageable) {
        return messageRepository.findAllByChannel_IdOrderByCreatedAtDesc(channelId, pageable);
    }

    @Override
    public boolean existsById(UUID id) {
        return messageRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannel_Id(channelId);
    }
}
