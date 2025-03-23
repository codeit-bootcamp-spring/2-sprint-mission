package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@EntityScan

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 103L;

    private final UUID id; //메세지 아이디
    private String message; //메세지내용
    private final ZonedDateTime createdAt;
    private ZonedDateTime updateAt;//
    private final UUID channelId; //채널 아이디
    private final UUID authorId; //작성한 사람
    // 메시지에 첨부  바이너리콘텐츠 ID 목록
    private final Set<UUID> attachmentIds = new HashSet<>();

    public Message(UUID channelId, UUID authorId, String message) {
        this.id = UUID.randomUUID();
        this.channelId = channelId; //어떤 채널
        this.authorId = authorId; //
        this.createdAt = ZonedDateTime.now();
        this.message = message; //메세지 내용
        this.updateAt = null;
    }
    
    // 첨부 파일 추가
    public void addAttachment(UUID attachmentId) {
        if (attachmentId != null) {
            this.attachmentIds.add(attachmentId);
            setUpdateAt();
        }
    }
    
    // 첨부 파일 제거
    public void removeAttachment(UUID attachmentId) {
        if (attachmentId != null && this.attachmentIds.remove(attachmentId)) {
            setUpdateAt();
        }
    }
    
    public void updateMessage(String message) {
        this.message=message;
        setUpdateAt();
    }
    public void setUpdateAt() {
        updateAt= ZonedDateTime.now();
    }
}



