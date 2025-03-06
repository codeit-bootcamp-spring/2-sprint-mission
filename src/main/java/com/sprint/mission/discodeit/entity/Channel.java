package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    public final Long createdAt;
    public Long updatedAt;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    private String name;


    public Channel(String name) {
        this(UUID.randomUUID(), System.currentTimeMillis(), name);
    }

    public Channel(UUID id, Long createdAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + this.getId() + '\'' +
                "name='" + name + '\'' +
                "creadAt='" + format.format(new Date(createdAt)) + '\'' +
                '}';
    }

    public UUID getId() {
        return id;
    }


    public Long getCreatedAt() {
        System.out.println("생성 시각: " + format.format(new Date(createdAt)));
        return createdAt;
    }

    public Long getUpdatedAt() {
        System.out.println("수정 시각: " + format.format(new Date(updatedAt)));
        return updatedAt;
    }
}
