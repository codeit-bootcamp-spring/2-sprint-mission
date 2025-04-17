package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.service.TimeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "channels")
@Getter
public class Channel extends BaseUpdatableEntity {

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ChannelType type;

    protected Channel() {
    }

    public Channel(String name, String description, ChannelType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
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
