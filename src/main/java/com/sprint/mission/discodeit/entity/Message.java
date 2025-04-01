package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 103L; // 직렬화 버전 UID 유지

    private final UUID id;
    private String message;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updateAt;
    private final UUID channelId;
    private final UUID authorId;
    private final Set<UUID> attachmentIds = new HashSet<>();

    public Message(UUID channelId, UUID authorId, String message) {
        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.authorId = authorId;
        this.createdAt = ZonedDateTime.now();
        this.message = message;
        this.updateAt = null; // 생성 시에는 수정 시간이 없음
    }

    // 첨부 파일 ID 추가
    public void addAttachment(UUID attachmentId) {
        if (attachmentId != null) {
            if (this.attachmentIds.add(attachmentId)) { // 실제로 추가되었을 때만 시간 업데이트
                setUpdateAt();
            }
        }
    }

    // 첨부 파일 ID 제거
    public void removeAttachment(UUID attachmentId) {
        if (attachmentId != null) {
            if (this.attachmentIds.remove(attachmentId)) { // 실제로 제거되었을 때만 시간 업데이트
                setUpdateAt();
            }
        }
    }

    // 메시지 내용 업데이트
    public void updateMessage(String message) {
        if (message != null && !message.equals(this.message)) { // 내용이 다를 경우에만 업데이트
            this.message = message;
            setUpdateAt();
        }
    }

    public void setUpdateAt() {
        this.updateAt = ZonedDateTime.now();
    }
}