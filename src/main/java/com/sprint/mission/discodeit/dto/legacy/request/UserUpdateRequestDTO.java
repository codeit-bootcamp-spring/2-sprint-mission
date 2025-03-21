package com.sprint.mission.discodeit.dto.legacy.request;

import com.sprint.mission.discodeit.dto.requestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.requestToService.UserUpdateDTO;

public record UserUpdateRequestDTO(
        UserUpdateDTO userUpdateDTO,
        BinaryContentCreateDTO binaryContentCreateDTO
) {
}
