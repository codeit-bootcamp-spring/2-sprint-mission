package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

import lombok.Getter;

@Getter
@Table(name = "channels")
@Entity
public class Channel extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    protected Channel() {
        super();
    }

    public Channel(ChannelType type, String name, String description, User owner) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public boolean update(String newName, String newDescription) {
        boolean updated = false;

        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            updated = true;
        }

        if (!Objects.equals(this.description, newDescription)) {
            this.description = newDescription;
            updated = true;
        }

        return updated;
    }

    public boolean isPublic() {
        return this.type == ChannelType.PUBLIC;
    }

    public boolean isPrivate() {
        return this.type == ChannelType.PRIVATE;
    }
}