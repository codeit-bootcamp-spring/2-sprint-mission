package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "read_statuses")
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  private Instant lastReadAt;

  public ReadStatus(User user, Channel channel, Instant lastReadTime) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadTime;
  }

  public void updateLastReadTime(Instant lastReadTime) {
    if (lastReadTime != null && !lastReadTime.equals(this.lastReadAt)) {
      this.lastReadAt = lastReadTime;
    }
  }
}
