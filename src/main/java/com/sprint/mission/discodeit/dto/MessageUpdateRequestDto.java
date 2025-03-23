package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
public class MessageUpdateRequestDto {
    private UUID messageId;
    private String newContent;
    private List<UUID> newAttachmentIds;

    public MessageUpdateRequestDto(UUID messageId, String newContent, List<UUID> newAttachmentIds) {
        this.messageId = messageId;
        this.newContent = newContent;
        this.newAttachmentIds = newAttachmentIds;
    }
}
