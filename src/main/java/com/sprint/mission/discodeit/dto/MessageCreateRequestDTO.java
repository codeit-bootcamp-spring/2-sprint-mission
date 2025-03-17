package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDTO {
    private UUID channelId;
    private UUID authorId;
    private UUID senderId;
    private String content;
    private List<byte[]> attachments;
}
