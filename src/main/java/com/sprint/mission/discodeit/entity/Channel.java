package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public static Channel createPrivate() {
        return new Channel(ChannelType.PRIVATE, null, null);
    }

    public static Channel createPublic(String name, String description) {
        return new Channel(ChannelType.PUBLIC, name, description);
    }

    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updateUpdatedAt();
        }
    }
}
