package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.dto.service.user.UserDTO;

import java.util.List;

public record UserListResponseDTO(
        List<UserDTO> userDTOList
) {
}
