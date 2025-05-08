package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "channel_id")
    @ManyToOne
    private Channel channel;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant lastReadAt;


    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void readStatusUpdate(Instant newLastReadAt) {
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), channel.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReadStatus readStatus)) return false;
        return user != null && channel != null &&
                user.getId().equals(readStatus.user.getId()) &&
                channel.getId().equals(readStatus.channel.getId());
    }
}
