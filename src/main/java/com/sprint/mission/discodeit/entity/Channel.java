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

    public void setOwner(User owner) {
        if (owner == null) {
            throw new IllegalArgumentException("유효하지 않은 오너입니다.");
        }
        this.owner = owner;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 서버 이름입니다.");
        }
        this.title = title;
    }

    public void setDescription(String description) {
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
}
