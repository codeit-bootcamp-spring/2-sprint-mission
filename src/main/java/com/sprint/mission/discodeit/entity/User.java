package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID uid;
    private final Long userCreateAt;
    private Long userUpdateAt;
    private final String userName;
    private String nickName;

    public User(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
        this.uid = UUID.randomUUID();
        this.userCreateAt = System.currentTimeMillis();
    }

    public UUID getUid() {
        return uid;
    }

    public Long getUserCreateAt() {
        return userCreateAt;
    }

    public Long getUserUpdateAt() {
        return userUpdateAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public Long userUpdateAtUpdate(){
        userUpdateAt = System.currentTimeMillis();
        return userUpdateAt;
    }

    public void nickNameUpdate(String userName){
        this.nickName = nickName;
    }

    @Override
    public String toString(){
        return "[uid: " + uid + ", userCreateAt: " + userCreateAt + ", userUpdateAt: " + userUpdateAt + ", userName: " + userName + "]";
    }

}
