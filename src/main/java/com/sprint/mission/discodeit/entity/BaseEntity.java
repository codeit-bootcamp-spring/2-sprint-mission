package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public BaseEntity() {
        id = UUID.randomUUID();
        createdAt = Instant.now().getEpochSecond();
        updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    // 생성자에서 초기화된 아이디를 사용자가 임의로 변경할 필요가 있는가
    public void updateId(UUID id) {
        this.id = id;
        updateUpdatedAt();
    }

    // createdAt을 수정하는 updateCreatedAt() 메소드를 만들 필요가 있을까?
    public void updateCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
        updateUpdatedAt();
    }

    // updatedAt을 사용자가 임의로 수정하는 updateUpdateAt() 메소드가 필요할까?
    // 현재시간으로 updatedAt 을 초기화하는 updateUpdateAt() 메소드만 필요한게 아닐까?
    public void updateUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
