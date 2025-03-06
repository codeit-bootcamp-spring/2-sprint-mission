package com.sprint.mission.discodeit.repository;

import java.util.UUID;

public interface MessageRepository {
    void messageSave(UUID channelUUID, UUID userUUID , String content);
}
