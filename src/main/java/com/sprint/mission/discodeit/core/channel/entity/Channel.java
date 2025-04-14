package com.sprint.mission.discodeit.core.channel.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Column(name = "name", length = 100)
  private String name;
  @Column(name = "description", length = 500)
  private String description;
  
  @Enumerated(EnumType.STRING)
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
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
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
