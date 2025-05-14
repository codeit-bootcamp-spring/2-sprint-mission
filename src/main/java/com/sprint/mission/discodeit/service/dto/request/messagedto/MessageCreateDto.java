package com.sprint.mission.discodeit.service.dto.request.messagedto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record MessageCreateDto(
        @Length(max = 1000)
        String content,
        UUID channelId,
        UUID authorId

) {

}
