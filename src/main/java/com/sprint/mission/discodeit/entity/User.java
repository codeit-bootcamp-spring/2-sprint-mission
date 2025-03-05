package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    public final Long createdAt;
    public Long updatedAt;
    protected final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    private String name;
    private String password;

    public User(String name, String password) {
        this(UUID.randomUUID(), System.currentTimeMillis(), name, password);
    }

    public User(UUID id, Long createdAt, String name, String password) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                "name='" + name + '\'' +
                "creadAt='" + dayTime.format(new Date(createdAt)) + '\'' +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
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
