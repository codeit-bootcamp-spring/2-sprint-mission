package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 103L;

    private final UUID id; //메세지 아이디
    private String message; //메세지내용
    private final Long createdAt;
    private Long updateAt;//
    private final UUID channelId;
    private final UUID authorId; //작성한 사람

    public Message(UUID channelId, UUID authorId, String message) {
        this.id = UUID.randomUUID();
        this.channelId = channelId; //어떤 채널
        this.authorId = authorId; //
        this.createdAt = new Date().getTime();
        this.message = message; //메세지 내용
        this.updateAt = null;
    }

    public String getMessage() {
        return message;
    }
    //메세지 내용 변경
    public void updateMessage(String message) {
        this.message=message;
        this.updateAt = new Date().getTime();
    }
    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }
    
    public void setUpdateAt() {
        this.updateAt = new Date().getTime();
    }
    
    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }
    
    public UUID getChannelId() {
        return channelId;
    }
    
    public UUID getAuthorId() {
        return authorId;
    }
}



