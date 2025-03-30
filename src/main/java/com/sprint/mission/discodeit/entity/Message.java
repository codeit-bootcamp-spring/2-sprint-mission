package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private final UUID userUUID;
    private final UUID channelUUID;
    private List<UUID> attachmentList;

    public void updateContent(String content) {
        super.updateTime();
        this.content = content;
    }

    public void updateAttachmentList(List<UUID> attachmentList) {
        super.updateTime();
        this.attachmentList = attachmentList;
    }
}
