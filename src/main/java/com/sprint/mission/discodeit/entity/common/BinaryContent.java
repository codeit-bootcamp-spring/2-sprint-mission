package com.sprint.mission.discodeit.entity.common;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {
    private final String resourceLink;

    public BinaryContent(String resourceLink) {
        super();
        this.resourceLink = resourceLink;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "resourceLink='" + resourceLink + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
