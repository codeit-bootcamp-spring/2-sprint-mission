package com.sprint.mission.discodeit.entity;

public class User extends CommonEntity {
    public User() {
        super();
    }

    @Override
    public String toString() {
        return "[USER ID: " + getId() + "\tCreated At: " + getCreatedAt() + "\tUpdated At: " + getUpdatedAt() + "]";

    }
}
