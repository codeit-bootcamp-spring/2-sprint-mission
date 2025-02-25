package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class BaseEntity {
    protected final UUID id;
    protected final Long createAt;
    protected Long updateAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void updateTime() {
        this.updateAt = System.currentTimeMillis();
    }

    public String formatTime(long time){
        LocalDateTime datetime = Instant.ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return datetime.format(formatter);
    }
}