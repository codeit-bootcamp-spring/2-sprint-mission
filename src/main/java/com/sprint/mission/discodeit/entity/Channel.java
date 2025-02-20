package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {
    private User owner;
    private String title;
    private String description;

    public Channel(User owner, String title, String description) {
        super();
        setOwner(owner);
        setTitle(title);
        setDescription(description);
    }

    private void setOwner(User owner) {
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

    public User getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void update(User owner, String title, String description) {
        if (owner != null) setOwner(owner);
        if (title != null) setTitle(title);
        if (description != null) setDescription(description);
    }
}
