package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity{
    private final UUID id;
    private final long createdAt; // 객체생성시간
    private long updatedAt; // 객체수정시간

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;

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

    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }

}
