package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private ChannelType type;

  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
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
