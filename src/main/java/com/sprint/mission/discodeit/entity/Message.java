package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends SharedEntity {
    private String content;
    private final UUID userKey;
    private final UUID channelKey;
    private final List<UUID> attachmentKeys;

    public Message(String content, UUID userKey, UUID channelKey, List<UUID> attachmentKeys) {
        super();
        this.content = content;
        this.userKey = userKey;
        this.channelKey = channelKey;
        this.attachmentKeys = attachmentKeys != null ? attachmentKeys : new ArrayList<>();
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(Instant.now());
    }

    public void addAttachment(UUID attachmentKey) {
        this.attachmentKeys.add(attachmentKey);
    }

    @Override
    public String toString() {
        return String.format("\n key= %s\n content= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, content, createdAt, updatedAt);
    }
}
