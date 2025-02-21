package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {
    private UUID owner;
    private String title;
    private String description;

    public Channel(UUID owner, String title, String description) {
        super();
        setOwner(owner);
        setTitle(title);
        setDescription(description);
    }

    private void setOwner(UUID owner) {
        if (owner == null) {
            throw new IllegalArgumentException("유효하지 않은 오너입니다.");
        }
        this.owner = owner;
    }

    private void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 서버 이름입니다.");
        }
        this.title = title;
    }

    private void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("유효하지 않은 서버 설명입니다.");
        }
        this.description = description;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void update(UUID owner, String title, String description) {
        if (owner != null) setOwner(owner);
        if (title != null) setTitle(title);
        if (description != null) setDescription(description);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "owner=" + owner +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                super.toString() +
                '}';
    }
}
