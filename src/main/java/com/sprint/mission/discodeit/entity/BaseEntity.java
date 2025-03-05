package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    public final Long createdAt;
    public Long updatedAt;
    protected final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public BaseEntity(UUID id, Long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = this.createdAt;
    }

    public BaseEntity() {
        this(UUID.randomUUID(), System.currentTimeMillis());
    }


    public UUID getId() {
        return id;
    }


    public Long getCreatedAt() {
        System.out.println("생성 시각: " + dayTime.format(new Date(createdAt)));
        return createdAt;
    }

    public Long getUpdatedAt() {
        System.out.println("수정 시각: " + dayTime.format(new Date(updatedAt)));
        return updatedAt;
    }
}
