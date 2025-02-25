package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BaseEntity {
    protected UUID id;

    public final Long createdAt;
    public Long updatedAt;
    protected final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
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
