package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateMessageDTO {
    private String message;
    private UUID userId;
    private UUID channelId;
    private List<AttachmentDTO> attachments;
}
