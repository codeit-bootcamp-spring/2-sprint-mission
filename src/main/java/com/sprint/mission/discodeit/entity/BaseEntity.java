package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BaseEntity {
    protected UUID id;  // 식별 ID
    protected long createdAt;  // 생성 시간
    protected long updatedAt;  // 수정 시간

    // 생성자: id, createdAt, updatedAt 초기화
    public BaseEntity() {
        this.id = UUID.randomUUID();  // 랜덤 UUID 생성
        this.createdAt = System.currentTimeMillis();  // 현재 시간(밀리초 단위)을 저장
        this.updatedAt = this.createdAt;  // 초기값은 처음 생성시간과 동일
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // 객체 수정 시 updatedAt 값 변경 (현재 시간으로)
    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}
