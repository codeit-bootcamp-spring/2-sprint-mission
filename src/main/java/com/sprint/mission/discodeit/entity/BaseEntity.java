package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BaseEntity {
    protected UUID id;
    protected String name;
    public final Long createdAt;
    public Long updatedAt;
    protected final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public BaseEntity(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreatedAt() {
        System.out.println("생성 시각: " + dayTime.format(new Date(createdAt)));
        return createdAt;
    }

    public Long getUpdatedAt() {
        System.out.println("수정 시각: " + dayTime.format(new Date(updatedAt)));
        return updatedAt;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "\nid='" + id + '\'' +
                ",\nname='" + name + '\'' +
                ",\ncreatedAt=" + dayTime.format(new Date(createdAt)) +
                ",\nupdatedAt=" + dayTime.format(new Date(updatedAt)) +
                '}';
    }
}
