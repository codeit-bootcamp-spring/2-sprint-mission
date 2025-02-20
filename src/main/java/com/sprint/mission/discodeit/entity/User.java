package com.sprint.mission.discodeit.entity;


import java.util.UUID;

public class User extends BaseEntity {
    public User(UUID id, long createdAt) {
        super(id, createdAt);
    }
}
