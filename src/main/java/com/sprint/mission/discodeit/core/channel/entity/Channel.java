package com.sprint.mission.discodeit.core.channel.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Channel extends BaseUpdatableEntity {

  private String name;
  private String description;

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
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      super.updateTime();
    }
  }

  //TODO. Channel Validator 구현해야 함
  public static class Validator {

    public static void validate(String name) {
      validateName(name);
    }

    public static void validateName(String name) {

    }
  }
}
