package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(
    name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "channel_id"})
    }
)
@Getter
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  public ReadStatus() {
    super();
  }

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    super();
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }
}
