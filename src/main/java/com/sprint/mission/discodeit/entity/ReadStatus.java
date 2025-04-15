package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "read_statuses", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "channel_id"})
})
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  @Builder
  public ReadStatus(Channel channel, User user, Instant lastReadAt) {
    super();
    this.channel = channel;
    this.user = user;
    this.lastReadAt = lastReadAt;
  }

  protected ReadStatus() {
  }

  public void updateLastAccessTime() {
    this.lastReadAt = Instant.now();
  }

  @Override
  public String toString() {
    return "ReadStatus{" +

        "id=" + getId() +
        ", channel=" + channel +
        ", user=" + user +
        ", readTime=" + lastReadAt +
        '}';
  }
}
