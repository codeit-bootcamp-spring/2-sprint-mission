package com.sprint.mission.discodeit.DTO.Request;

import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserCreateDTO;

public record UserRegisterRequestDTO(
        UserCreateDTO userCreateDTO,
        BinaryContentCreateDTO binaryContentCreateDTO
) {
}
