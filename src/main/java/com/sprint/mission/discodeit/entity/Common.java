package com.sprint.mission.discodeit.entity;

import java.util.UUID;

// 겹치는 필드, 메소드 등을 Common으로 따로 빼주는게 좋을 것 같음
public abstract class Common {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        // 생성 시점엔 updateAt과 createdAt이 같다.
        this.updatedAt = createdAt;
    }

    // id와 createdAt은 생성 후 바뀔 일이 없다.

    // 수정 날짜 수정
    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
