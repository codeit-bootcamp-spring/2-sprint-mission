package com.sprint.mission.discodeit.dto.controller.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelRequestDTO(
    List<UUID> participantIds
) {

}
