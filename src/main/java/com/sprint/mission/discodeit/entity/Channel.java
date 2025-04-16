package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;


    public Channel(ChannelType type, String channelName, String description) {
        this.type = type;
        this.name = channelName;
        this.description = description;
    }


    public void updateChannel(String channelName, String description) {
        this.name = channelName;
        this.description = description;
        System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", this.name, this.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Channel channel) {
            return channel.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nChannel Type: " + type +
                "\nChannelName: " + name + "\nDescription: " + description +
                "\nChannel ID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAt() +
                "\nUpdatedAt: " + this.getUpdatedAt();
    }
}
