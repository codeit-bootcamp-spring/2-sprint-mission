package com.sprint.mission.discodeit.entity;

import java.time.Instant;

public class User extends SharedEntity{
    private String id;
    private final String name;
    private String pwd;
    private String email;
    private String phone;

    public User(String id, String name, String pwd, String email, String phone) {
        super();
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void updateId(String id) {
        this.id = id;
        setUpdatedAt(Instant.now().toEpochMilli());
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public void updatePwd(String pwd) {
        this.pwd = pwd;
        setUpdatedAt(Instant.now().toEpochMilli());
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
        setUpdatedAt(Instant.now().toEpochMilli());
    }

    public String getPhone() {
        return phone;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
        setUpdatedAt(Instant.now().toEpochMilli());
    }

    @Override
    public String toString() {
        return String.format("\n uuid: %s\n userId: %s\n userName: %s\n userPwd: %s\n userEmail: %s\n userPhone: %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, id, name, pwd, email, phone, createdAt, updatedAt);
    }
}
