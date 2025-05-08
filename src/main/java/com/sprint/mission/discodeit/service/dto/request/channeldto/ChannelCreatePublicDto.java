package com.sprint.mission.discodeit.service.dto.request.channeldto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChannelCreatePublicDto(
        @NotBlank String name,

        @Length(max = 200)
        String description

) {
}
