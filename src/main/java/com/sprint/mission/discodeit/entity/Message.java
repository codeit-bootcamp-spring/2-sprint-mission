package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String content;
  private User author;
  private Channel channel;
  private List<UUID> attachmentIds;

  public void updateContent(String content) {
    this.content = content;
  }
}
