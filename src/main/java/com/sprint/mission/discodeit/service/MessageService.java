package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MessageService {
    UUID createMessage(MessageCreateRequest messageCreateRequest);
    Message readMessage(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    void updateMessage(UUID id, MessageUpdateRequest messageUpdateRequest);
    void deleteBinaryContentInMessage(UUID messageId, UUID binaryContentId);
    void deleteMessage(UUID messageId);
    static void validateMessageId(UUID messageId, MessageRepository jcfMessageRepository) {
        if (!jcfMessageRepository.existsById(messageId)) {
            throw new NoSuchElementException("해당 messageId를 가진 사용자를 찾을 수 없습니다 : " + messageId);
        }
    }
}
