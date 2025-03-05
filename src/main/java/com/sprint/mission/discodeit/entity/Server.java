package com.sprint.mission.discodeit.entity;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Server  implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    public final Long createdAt;
    public Long updatedAt;
    private String name;

    private final SimpleDateFormat dayTime;

    public Server(String name) {
        this(UUID.randomUUID(),System.currentTimeMillis(),name);
    }

    public Server(UUID id, Long createdAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
        this.dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + this.getId() + '\'' +
                "name='" + name + '\'' +
                "creadAt='" + dayTime.format(new Date(createdAt)) + '\'' +
                '}';
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
