package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.utils.TimeUtil;
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
    protected final Long createdAt;
    protected Long updatedAt;


    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
    }

    public String getCreatedAtFormatted() {
        return TimeUtil.convertToFormattedDate(createdAt);

    }

    public String getUpdatedAttFormatted() {
        return TimeUtil.convertToFormattedDate(updatedAt);
    }

    // 업데이트 메소드
    public void update() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

}
