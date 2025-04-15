package com.sprint.mission.discodeit.entity;

// 필요한 import 추가

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects; // equals/hashCode 위해 남겨둘 수 있음 (혹시 부모 클래스에 없다면)
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "channels")
public class Channel extends BaseEntity { // <<<--- 부모 클래스 상속 (클래스 이름은 실제 사용하는 것으로 변경)

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;


    @OneToMany(
        mappedBy = "channel",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private final Set<UserChannel> participants = new HashSet<>();


    protected Channel() {
        super(); // 부모 클래스 생성자 호출
    }

    // 공개 채널
    public Channel(String name, String description) {
        super();
        this.type = ChannelType.PUBLIC;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Channel name cannot be empty.");
        }
        this.name = name;
        this.description = description;
    }

    //설명 x
    public Channel(String name) {
        this(name, null);
    }

    public boolean update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.isEmpty() && !newName.equals(this.name)) {
            if (newName.isBlank()) {
                throw new IllegalArgumentException("Channel name cannot be empty.");
            }
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription == null && this.description != null) {
            this.description = null;
            anyValueUpdated = true;
        } else if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }
        return anyValueUpdated;
    }


    public boolean isPublic() {
        return this.type == ChannelType.PUBLIC;
    }

    public boolean isPrivate() {
        return this.type == ChannelType.PRIVATE;
    }


    public void leaveChannel(User user) {

        if (user == null) {
            return;
        }
        for (Iterator<UserChannel> iterator = this.channelUsers.iterator(); iterator.hasNext(); ) {
            UserChannel userChannel = iterator.next();
            if (userChannel.getUser() != null && userChannel.getUser().equals(user)) {
                iterator.remove();
                return;
            }
        }
    }

    public void addParticipant(User user) {

        if (user == null) {
            return;
        }
        boolean alreadyExists = this.channelUsers.stream()
            .anyMatch(uc -> uc.getUser() != null && uc.getUser().equals(user));
        if (!alreadyExists) {
            UserChannel userChannel = new UserChannel(user, this);
            this.channelUsers.add(userChannel);
        }
    }


}