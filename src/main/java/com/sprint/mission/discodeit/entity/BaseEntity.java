package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    protected final UUID id;
    protected Instant createdAt;
    protected Instant updatedAt;


    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    // 업데이트 메소드
    public void update() {
        this.updatedAt = Instant.now();
    }

}
