package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;

@Getter
@Table(name = "channels")
@Entity
public class Channel extends BaseEntity { // 부모 클래스 상속 가정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // 타입은 필수
    private ChannelType type;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @OneToMany(
        mappedBy = "channel",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private final Set<UserChannel> participants = new HashSet<>();

    protected Channel() {
        super();
    }


    public Channel(ChannelType type, String name, String description) {
        super();
        if (type == null) {
            throw new IllegalArgumentException("Channel type cannot be null.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Channel name cannot be empty.");
        }
        this.type = type;
        this.name = name.trim();
        this.description =
            (description == null || description.isBlank()) ? null : description.trim();
    }

    public static Channel createPublicChannel(String name, String description) {
        return new Channel(ChannelType.PUBLIC, name, description);
    }

    public static Channel createPublicChannel(String name) {
        return new Channel(ChannelType.PUBLIC, name, null);
    }


    public static Channel createPrivateChannel(String name, String description) {
        return new Channel(ChannelType.PRIVATE, name, description);
    }

    public static Channel createPrivateChannel(String name) {
        return new Channel(ChannelType.PRIVATE, name, null);
    }


    public boolean update(String newName, String newDescription) {
        boolean updated = false;
        String trimmedNewName = (newName == null) ? null : newName.trim();
        String trimmedNewDescription = (newDescription == null) ? null : newDescription.trim();

        if (trimmedNewName != null && trimmedNewName.isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be empty.");
        }

        if (trimmedNewName != null && !trimmedNewName.equals(this.name)) {
            this.name = trimmedNewName;
            updated = true;
        }

        if (!Objects.equals(this.description, trimmedNewDescription)) {
            this.description = trimmedNewDescription;
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

    public void addParticipant(User user) {
        if (user == null) {
            return;
        }
        boolean alreadyExists = this.participants.stream()
            .anyMatch(uc -> uc != null && uc.getUser() != null && uc.getUser().equals(user));
        if (!alreadyExists) {
            UserChannel userChannel = new UserChannel(user, this);
            this.participants.add(userChannel);
        }
    }

    public void leaveChannel(User user) {
        if (user == null) {
            return;
        }
        this.participants.removeIf(
            uc -> uc != null && uc.getUser() != null && uc.getUser().equals(user));
    }

}