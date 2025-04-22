package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "read_statues")
public class ReadStatus extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(name = "last_read_at")
    private Instant lastReadTime;

    protected ReadStatus() {
    }

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }

    public void updateLastReadTime() {
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }
}
