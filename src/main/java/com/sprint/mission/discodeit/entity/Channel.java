package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
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


    protected Channel() {
        super();
    }


    public Channel(ChannelType type, String name, String description) {
        super();
        if (type == null) {
            throw new IllegalArgumentException("Channel type cannot be null.");
        }

        this.type = type;
        this.name = (name != null) ? name.trim() : null; // 이름이 null일 수 있도록 수정
        this.description =
            (description == null || description.isBlank()) ? null : description.trim();
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


}