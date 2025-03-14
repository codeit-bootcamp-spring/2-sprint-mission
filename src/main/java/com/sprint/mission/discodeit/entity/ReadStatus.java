package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID channelId;

    public void updateTime() {
        super.updateTime();
    }
}
