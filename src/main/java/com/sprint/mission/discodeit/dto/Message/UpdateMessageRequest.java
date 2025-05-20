package com.sprint.mission.discodeit.dto.Message;

import jakarta.validation.constraints.NotBlank;

public record UpdateMessageRequest(

    @NotBlank
    String newContent
) {

}
