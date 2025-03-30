package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponseDTO {
    private UUID userId;
    private UUID channelId;
    private UUID messageId;
    private String message;
    private List<AttachmentDTO> attachments;
}
