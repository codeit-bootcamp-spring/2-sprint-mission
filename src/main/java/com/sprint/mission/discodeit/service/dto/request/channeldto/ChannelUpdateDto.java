package com.sprint.mission.discodeit.service.dto.request.channeldto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record   ChannelUpdateDto(
        @NotBlank String newName,

        @Length(max = 200)
        String newDescription
) {

}
