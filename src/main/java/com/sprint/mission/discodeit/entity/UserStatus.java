package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    UUID userUUID;
    public void updateLastStatus() {
        super.updateTime();
    }
}
