package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record CreatePublicChannelRequest(

    @Size(max = 100)
    String name,
    String description
) {

}
