package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID(); // 무작위 UUID를 생성
        this.createdAt = System.currentTimeMillis(); // 현재 시간을 밀리초 단위로 변환하는 메서드
        this.updatedAt = this.createdAt; // updatedAt 을 초기화하지 않으면, 나중에 반환될 때 0이 될 수 있다.
    }

    //Getter 메서드

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
