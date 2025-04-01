package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository {

    void reset();
    Message save(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);

    Message update(Message message,  UpdateMessageDTO updateMessageDTO);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
//
//    List<Message> findAllByMessageId(UUID messageId);
//
//    void remove(UUID messageId);
}
