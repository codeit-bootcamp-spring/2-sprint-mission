package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "channels")
@NoArgsConstructor
@SuperBuilder
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  public static Channel create(ChannelType type, String name, String description) {
    validate(type, name, description);
    return Channel.builder()
        .type(type)
        .name(name)
        .description(description)
        .build();
  }

  public void update(String name, String description) {
    if (name != null && !name.equals(this.name)) {
      validateName(name);
      this.name = name;
    }
    if (description != null && !description.equals(this.description)) {
      validateDescription(description);
      this.description = description;
    }
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(ChannelType type, String name, String description) {
    if (type == null) {
      throw new IllegalArgumentException("채널 Type 값이 없습니다.");
    }
    if (type == ChannelType.PUBLIC && (name == null || name.trim().isEmpty())) {
      throw new IllegalArgumentException("채널명이 없습니다.");
    }
    validateName(name);
    validateDescription(description);
  }

  private static void validateName(String name) {
    if (name != null && name.length() > 100) {
      throw new IllegalArgumentException("채널명은 100자 미만이어야 합니다.");
    }
  }

  private static void validateDescription(String description) {
    if (description != null && description.length() > 500) {
      throw new IllegalArgumentException("채널 설명은 500자 미만이어야 합니다.");
    }
  }

}
