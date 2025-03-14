package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    //
    private String content;
    //
    private final UUID channelId;
    private final UUID authorId;

    public Message(String content, UUID channelId, UUID authorId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        //
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }
}
