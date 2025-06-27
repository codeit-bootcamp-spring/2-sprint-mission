package com.sprint.mission.discodeit.core.channel.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "channels")
@Entity
public class Channel extends BaseUpdatableEntity {

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ChannelType type;

  private Channel(String name, String description, ChannelType type) {
    super();
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public static Channel create(String name, String description, ChannelType type) {
    return new Channel(name, description, type);
  }

  public void update(String newName, String newDescription) {
    validateType(type);
    this.name = updateField(this.name, newName);
    this.description = updateField(this.description, newDescription);
  }

  private <T> T updateField(T target, T replace) {
    if (replace != null && !replace.equals(target)) {
      return replace;
    } else {
      return target;
    }
  }

  public static void validateType(ChannelType type) {
    if (type == ChannelType.PRIVATE) {
      throw new ChannelUnmodifiableException(ErrorCode.UNMODIFIABLE_ERROR);
    }
  }
}
