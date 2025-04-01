package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;
import lombok.Getter;

@Getter
public class MessageUpdateRequestDto {
    private UUID messageId;
    private String newContent;
}
