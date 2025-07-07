package com.sprint.mission.discodeit.entity;

public enum Role {
    ROLE_USER,
    ROLE_CHANNEL_MANAGER,
    ROLE_ADMIN;

    public boolean higherThan(Role other) {
        return this.ordinal() > other.ordinal();
    }
}