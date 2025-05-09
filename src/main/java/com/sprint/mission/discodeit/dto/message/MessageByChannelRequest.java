package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public record MessageByChannelRequest(
        @RequestParam @NotNull UUID channelId,
        @RequestParam Integer size
) {

    public MessageByChannelRequest {
        if (size == null) {
            size = 50;
        }
    }
}
