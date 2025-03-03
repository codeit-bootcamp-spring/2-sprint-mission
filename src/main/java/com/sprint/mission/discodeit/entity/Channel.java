package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity {
    private UUID ownerId;
    private String title;
    private String description;

    public Channel(UUID ownerId, String title, String description) {
        super();
        setOwnerId(ownerId);
        setTitle(title);
        setDescription(description);
    }

    private void setOwnerId(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("유효하지 않은 오너입니다.");
        }
        this.ownerId = ownerId;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void update(UUID ownerId, String title, String description) {
        if (ownerId != null) setOwnerId(ownerId);
        if (title != null) setTitle(title);
        if (description != null) setDescription(description);

        updateModifiedAt();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ownerId=" + ownerId +
                super.toString() +
                '}';
    }
}
