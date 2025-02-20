package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    private static int count;
    private final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public final Long createdAt;
    public Long updatedAt;

    protected final String id;
    protected String name;

    public final Map<String,Channel> channels;
    public final Map<String,User> users;

    public Server(String name) {
        this.id = "S" + count++;
        this.name = name;

        this.channels = new HashMap<>();
        this.users = new HashMap<>();

        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public String getId() {
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

    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String,Channel> getChannels() {
        return channels;
    }

    public Map<String,User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Server{" +
                "\ncreatedAt=" + dayTime.format(new Date(createdAt)) +
                ",\nupdatedAt=" + dayTime.format(new Date(updatedAt)) +
                ",\nid='" + id + '\'' +
                ",\nname='" + name + '\'' +
                '}';
    }
}
