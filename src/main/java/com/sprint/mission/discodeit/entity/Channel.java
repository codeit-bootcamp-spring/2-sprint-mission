package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "channels")
@AllArgsConstructor
public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;


  @Builder
  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  protected Channel() {
  }

  public void updateChannelType(ChannelType type) {
    this.type = type;
    updateTimestamp();
  }

  public void updateDescription(String description) {
    this.description = description;
    updateTimestamp();
  }

  public void updateChannelName(String channelName) {
    this.name = channelName;
    updateTimestamp();
  }


  @Override
  public String toString() {
    return "Channel{" +
        "channelId='" + getId() + '\'' +
        "channelName='" + name + '\'' +
        ", description='" + description + '\'' +
        ", channelType=" + type + '\'' +
        ", lastUpdateTime= " + getUpdatedAt() +
        '}';
  }
}
