package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMessageRequest {

    private String content;
    private UUID channelId;
    private UUID authorId;
}
