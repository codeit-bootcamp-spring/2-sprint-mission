package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateChannelRequest(

    @NotNull
    @Size(max = 100)
    String newName,
    String newDescription
) {

}
