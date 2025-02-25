package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String password;

    public User(String id, String name, String password) {
        super(id, name);
        this.password = password;
    }


}
