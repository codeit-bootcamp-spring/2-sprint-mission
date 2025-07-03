package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
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

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    protected Channel() {
        super();
    }

    public Channel(ChannelType type, String name, String description, UUID ownerId) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
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