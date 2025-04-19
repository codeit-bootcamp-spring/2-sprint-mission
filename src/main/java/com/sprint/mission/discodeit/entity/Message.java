package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID channelId;
    private final UUID userId;
    private String context;
    private List<UUID> attachmentIds;

    public Message(String context, UUID channelId, UUID userId, List<UUID> attachmentIds) {
        this.userId = userId;
        this.channelId = channelId;
        this.context = context;
        this.attachmentIds = attachmentIds;
    }

    public void updateContext(String context) {
        this.context = context;
    }
}
