package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private static int count;
    private final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public final Long createdAt;
    public  Long updatedAt;
    private final String id;

    private String name;
    private String password;
    private UserStatus userStatus;

//    나중에 추가할 기능
//    private String email;
//    private String birthday;

    public User(String name, String password) {
        this.id = "U" + count++;
        this.name = name;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userStatus = UserStatus.ONLINE;
    }

    public String getId() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "\ncreatedAt=" + dayTime.format(new Date(createdAt)) +
                ",\nupdatedAt=" + dayTime.format(new Date(updatedAt)) +
                ",\nid='" + id + '\'' +
                ",\nname='" + name + '\'' +
                '}';
    }
}
