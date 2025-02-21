package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

public class Message extends BaseEntity{
    private String text;

    public Message(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createdAt=" + TimeFormatter.format(createdAt) +
                ", updatedAt=" + TimeFormatter.format(updatedAt) +
                '}';
    }
}