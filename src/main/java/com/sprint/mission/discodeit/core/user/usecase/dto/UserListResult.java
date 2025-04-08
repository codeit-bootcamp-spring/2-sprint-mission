package com.sprint.mission.discodeit.core.user.usecase.dto;

import java.util.List;

public record UserListResult(
    List<UserResult> userList
) {

}
