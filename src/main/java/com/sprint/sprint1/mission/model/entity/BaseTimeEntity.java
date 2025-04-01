package com.sprint.sprint1.mission.model.entity;



public abstract class BaseTimeEntity {

    private long created;
    private long updatedAt;

    public BaseTimeEntity() {
        this.created = System.currentTimeMillis();
        this.updatedAt = this.created;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }


    public long getCreated() {
        return created;
    }

    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }

}
