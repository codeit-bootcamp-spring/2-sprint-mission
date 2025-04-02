package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public void update(String name, Instant updatedAt) {
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public boolean isPrivate() {
        return "PRIVATE CHANNEL".equals(this.name);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
