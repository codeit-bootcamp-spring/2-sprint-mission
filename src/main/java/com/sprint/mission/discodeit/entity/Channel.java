package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses;

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
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
        }
    }
}
