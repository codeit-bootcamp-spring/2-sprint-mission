package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message extends Entity  {
    private String content;
    private final UUID authorId;
    private final UUID channelId;


    public Message(String content, UUID channelId,UUID authorId) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
    }

    public String getContent() { return content; }
    public UUID getauthorId() { return authorId; }
    public UUID getChannelId() { return channelId; }

    public void updateContent(String content) {
        if(content != null) {
            this.content = content;
            this.updatedAt = System.currentTimeMillis();
        } else return;;
    }
}