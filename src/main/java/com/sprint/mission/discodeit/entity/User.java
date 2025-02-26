package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.Repository.UserRepository;

import java.util.UUID;


public class User extends BaseEntity {
    private String password;
    private String name;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
