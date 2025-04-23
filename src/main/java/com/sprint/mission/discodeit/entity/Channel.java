package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "channels")
@Getter
@Setter
public class Channel extends BaseUpdatableEntity implements Persistable<UUID> {

    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    private String name;

    private String description;

    @Transient
    private boolean isNew = true;

    protected Channel() {
    }

    public static Channel createPublicChannel(String name, String description) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setDescription(description);
        channel.setChannelType(ChannelType.PUBLIC);

        return channel;
    }

    public static Channel createPrivateChannel() {
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.PRIVATE);

        return channel;
    }

    public void update(String newName, String newDescription) {
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
        }

        if (newDescription != null && newDescription.equals((this.description))) {
            this.description = newDescription;
        }
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PrePersist
    public void markNotNew() {
        this.isNew = false;
    }
}
