package com.sprint.mission.discodeit.core.channel.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.channel.exception.ChannelInvalidRequestException;
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
    Validator.validate(name, description);
    return new Channel(name, description, type);
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      Validator.validateName(newName);
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      Validator.validateDescription(newDescription);
      this.description = newDescription;
    }
  }

  public static class Validator {

    //TODO 정규패턴을 사용해서 유효성 검증할 예정 => 채널 이름 조건, 설명 조건 등
    public static void validate(String name, String description) {
      validateName(name);
      validateDescription(description);
    }

    public static void validateName(String name) {
      if (name == null || name.isBlank() || name.length() > 50) {
        throw new ChannelInvalidRequestException(ErrorCode.CHANNEL_INVALID_REQUEST, name);
      }
    }

    public static void validateDescription(String description) {
      if (description == null || description.isBlank() || description.length() > 50) {
        throw new ChannelInvalidRequestException(ErrorCode.CHANNEL_INVALID_REQUEST, description);
      }
    }
  }
}
