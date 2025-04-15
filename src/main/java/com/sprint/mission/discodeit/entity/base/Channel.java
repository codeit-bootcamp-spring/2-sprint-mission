package com.sprint.mission.discodeit.entity.base;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 10)
  private ChannelType type;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  protected Channel() {
  }

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }
}
