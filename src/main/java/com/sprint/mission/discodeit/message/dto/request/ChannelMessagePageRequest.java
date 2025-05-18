package com.sprint.mission.discodeit.message.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public record ChannelMessagePageRequest(
        @RequestParam
        @NotNull
        UUID channelId,
        @RequestParam
        Integer size
) {

    public ChannelMessagePageRequest {
        if (size == null) {
            size = 50;
        }
    }

}
