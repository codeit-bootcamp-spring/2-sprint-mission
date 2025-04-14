package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "channels")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ChannelType type;

  public void updateChannelName(String name) {
    this.name = name;
  }

  public void updateChannelDescription(String description) {
    this.description = description;
  }
}
