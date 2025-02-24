package com.sprint.mission.discodeit.entity;

public class Message extends CommonEntity {
    public Message() {
        super();
    }

    @Override
    public String toString() {
        return "[Message ID: " + getId() + "\tCreated At: " + getCreatedAt() + "\tUpdated At: " + getUpdatedAt() + "]";
    }
}
