package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class commonness {
    public UUID id;
    public Long createdAt;
    public Long updatedAt;

    public commonness() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdateAT(){
        updatedAt = System.currentTimeMillis();
    }
}
