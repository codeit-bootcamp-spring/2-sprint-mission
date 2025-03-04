package com.sprint.mission.discodeit.entity;

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
