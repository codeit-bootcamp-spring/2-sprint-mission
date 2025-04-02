package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserResult;
import java.util.List;

public record UserDisplayList(
    List<UserResult> users
) {

}
