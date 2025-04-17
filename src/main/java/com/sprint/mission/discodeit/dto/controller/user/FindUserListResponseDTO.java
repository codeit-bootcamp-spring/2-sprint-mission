package com.sprint.mission.discodeit.dto.controller.user;


import java.util.List;

public record FindUserListResponseDTO(
    List<FindUserResponseDTO> userDTOList
) {

}
