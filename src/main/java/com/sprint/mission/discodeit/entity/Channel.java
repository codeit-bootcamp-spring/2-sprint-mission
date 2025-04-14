package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class Channel extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  private ChannelType type;
  private String name;
  private String description;

  @Builder
  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public static Channel ofPublic(String name, String description) {
    return Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(name)
        .description(description)
        .build();
  }

  public static Channel ofPrivate() {
    return Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
  }


  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }

}
