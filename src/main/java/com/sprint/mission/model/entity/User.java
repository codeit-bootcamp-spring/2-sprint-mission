package com.sprint.mission.model.entity;

import java.util.UUID;

public class User extends BaseTimeEntity{


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    private UUID id;
    private String username;
    private String password;
    private String email;


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

    public void update(String password, String username) {
        if (password != null) {
            this.password = password;
        }
        if (username != null) {
            this.username = username;
        }
    }

    public String findByEmail(String email) {
        if(this.email.equals(email)) {
            return this.id.toString();
        }
        return email + "는 없는 이메입니다";
    }

}
