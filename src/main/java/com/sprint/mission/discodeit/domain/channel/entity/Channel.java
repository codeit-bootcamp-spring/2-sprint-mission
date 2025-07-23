package com.sprint.mission.discodeit.domain.channel.entity;

import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.exception.PrivateChannelUpdateForbiddenException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Entity
@Table(name = "channels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  private ChannelType type;

  private Channel(ChannelType channelType, String name, String description) {
    this.name = name;
    this.description = description;
    this.type = channelType;
  }

  public static Channel createPublic(String name, String description) {
    return new Channel(ChannelType.PUBLIC, name, description);
  }

  public static Channel createPrivate() {
    return new Channel(ChannelType.PRIVATE, null, null);
  }

  public void update(String name, String description) {
    if (this.type == ChannelType.PRIVATE) {
      throw new PrivateChannelUpdateForbiddenException(Map.of());
    }

    if (description != null && !description.equals(this.description)) {
      this.description = description;
    }

    if (name != null && !name.equals(this.name)) {
      this.name = name;
    }
  }

  public boolean isPrivate() {
    return this.type.equals(ChannelType.PRIVATE);
  }

  public boolean isPublic() {
    return this.type.equals(ChannelType.PUBLIC);
  }

}
