package com.sprint.mission.discodeit.service.dto.request.messagedto;

import org.hibernate.validator.constraints.Length;

public record MessageUpdateDto(
        @Length(max = 1000)
        String newContent
) {

}
