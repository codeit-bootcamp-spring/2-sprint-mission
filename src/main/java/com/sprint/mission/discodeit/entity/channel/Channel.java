package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Channel extends BaseUpdatableEntity {

  private final ChannelType type;

  @Nullable
  private String name; //PUBLIC 전용
  @Nullable
  private String description; //PUBLIC 전용


  //Private Channel 만들때 호출
  public Channel(ChannelType type) {
    this(type, null, null);
  }

  //Public Channel 만들때 호출
  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;

    //PUBLIC 전용
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      markUpdated();
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      markUpdated();
    }
  }

  @Override
  public String toString() {
    return "Channel{" +
        "type=" + type +
        ", name='" + name + '\'' +
        ", newDescription='" + description + '\'' +
        ", updatedAt=" + updatedAt +
        ", id=" + id +
        ", createdAt=" + createdAt +
        '}';
  }
}
