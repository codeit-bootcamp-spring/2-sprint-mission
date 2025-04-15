package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MessageAttachmentId implements Serializable {

  private UUID messageId;
  private UUID attachmentId;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MessageAttachmentId that)) {
      return false;
    }
    return Objects.equals(messageId, that.messageId) && Objects.equals(
        attachmentId, that.attachmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, attachmentId);
  }
}
