package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;

    public User(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void update(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "< 유저 > " + name + ", " + getId();
    }
}
