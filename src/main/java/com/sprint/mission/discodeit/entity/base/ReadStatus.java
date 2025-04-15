package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;

@Entity
@Getter
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
  private User user;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  protected ReadStatus() {
  }

  public ReadStatus(User user, Channel channel) {
    this.user = user;
    this.channel = channel;
  }

  public void updateLastReadAt() {
    this.lastReadAt = Instant.now();
  }
}
