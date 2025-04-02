package com.sprint.discodeit.sprint5.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Message implements Serializable {


    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String content;
    //
    private UUID channelId;
    private UUID authorId;
    private List<UUID>  attachmentIds;

    @Builder
    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = null;
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        }
    }
}
