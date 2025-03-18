package com.sprint.mission.discodeit.DTO.Request;

import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserUpdateDTO;

public record UserUpdateRequestDTO(
        UserUpdateDTO userUpdateDTO,
        BinaryContentCreateDTO binaryContentCreateDTO
) {
}
