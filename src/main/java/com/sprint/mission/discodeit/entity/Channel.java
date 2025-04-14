package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private ChannelType type;

  public void updateChannelName(String name) {
    this.name = name;
  }

  public void updateChannelDescription(String description) {
    this.description = description;
  }
}
