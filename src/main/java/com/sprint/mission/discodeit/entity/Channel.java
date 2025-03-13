package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id + '\'' +
                ", type=" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
