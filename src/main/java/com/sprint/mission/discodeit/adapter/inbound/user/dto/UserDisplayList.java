package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserFindDTO;
import java.util.List;

public record UserDisplayList(
    List<UserFindDTO> users
) {

}
