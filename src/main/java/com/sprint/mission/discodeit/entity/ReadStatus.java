package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus {
    private final UUID id;
    private final Instant createdAtSeconds;
    private Instant updatedAtSeconds;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.updatedAtSeconds = createdAtSeconds;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt (Instant newLastReadAt){
        if(newLastReadAt != null && newLastReadAt.isAfter(this.lastReadAt)){
            this.lastReadAt = newLastReadAt;
            this.updatedAtSeconds = Instant.now();
        }
    }

    //안 읽은 메시지 개수 조회
//    public int getUnreadMessageCount (MessageRepository messageRepository){
//        return messageRepository.countUnreadmessages(this.channelId, this.lastReadAt);

}
