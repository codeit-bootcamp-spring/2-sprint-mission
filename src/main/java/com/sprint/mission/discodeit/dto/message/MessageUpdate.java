package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;
import lombok.Getter;

@Getter
public class MessageUpdate {
    private UUID messageId;
    private String newContent;
}
