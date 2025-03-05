package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

public class User extends BaseEntity {
    private String name;


    public User(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + TimeFormatter.format(createdAt, "yyyy-MM-dd HH:mm:ss") +
                ", updatedAt=" + TimeFormatter.format(updatedAt, "yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}
