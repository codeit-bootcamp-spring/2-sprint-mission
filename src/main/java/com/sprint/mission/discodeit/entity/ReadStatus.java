package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @JoinColumn(name = "user_id", unique = true)
    @ManyToOne
    private User user;

    @JoinColumn(name = "channel_id", unique = true)
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
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + user +
                "\nChannel ID: " + channel +
                "\nLast Read Time: " + getLastReadAt();
    }

}
