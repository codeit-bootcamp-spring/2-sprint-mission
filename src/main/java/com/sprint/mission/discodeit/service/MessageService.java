package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface MessageService {
    UUID createMessage(UUID userId, UUID channelId);

    void searchMessage(UUID id);

    void searchAllMessages();

    void updateMessage(UUID id);

    void deleteMessage(UUID id);
}
