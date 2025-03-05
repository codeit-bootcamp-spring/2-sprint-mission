package com.sprint.mission.discodeit.entity;

import java.util.Date;
import java.util.UUID;

public class User extends Entity {
    private String username;
    private String password;
    private String email;

    public User(UUID uuid, String username, String password, String email) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public String getUsername() { return username; }
    public void updateUsername(String username) {
        if(username != null){
            this.username = username;
            this.updatedAt = System.currentTimeMillis();
        }else return;
    }
    public void setPassword(String password) {
        if(password != null){
            this.password = password;
            this.updatedAt = System.currentTimeMillis();
        }else return;;

    }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if(email != null){
            this.email = email;
            this.updatedAt = System.currentTimeMillis();
        }else return;;
    }
}