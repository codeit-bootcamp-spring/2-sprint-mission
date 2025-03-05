package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    public final Long createdAt;
    public Long updatedAt;

    private String str;

    private final SimpleDateFormat dayTime;

    public Message(String str) {
        this(UUID.randomUUID(), System.currentTimeMillis(), str);
    }

    public Message(UUID id, Long createdAt, String str) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.str = str;
        this.dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + this.getId() + '\'' +
                "str='" + str + '\'' +
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
