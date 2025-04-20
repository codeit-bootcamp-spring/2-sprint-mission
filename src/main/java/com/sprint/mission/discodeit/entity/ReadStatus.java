package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"})
)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_read_status_user"))
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_read_status_channel"))
  private Channel channel;

  @Column(nullable = false)
  private Instant lastReadAt;

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      super.setUpdatedAt();
    }
  }
}