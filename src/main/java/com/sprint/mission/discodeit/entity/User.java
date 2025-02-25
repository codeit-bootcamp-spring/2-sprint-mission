package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.Repository.UserRepository;


public class User extends BaseEntity {
    private String password;
    private UserRepository userRepository;

    public User(String name, String password) {
        super(name);
        this.password = password;
        userRepository = new UserRepository();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
