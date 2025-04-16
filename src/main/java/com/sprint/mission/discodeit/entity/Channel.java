package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;

@Getter
public class Channel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private ChannelType type;

    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description, Instant updatedAt) {
        this.name = name;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public boolean isPrivate() {
        return this.type == ChannelType.PRIVATE;
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
