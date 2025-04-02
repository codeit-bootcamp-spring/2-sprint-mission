package com.sprint.mission.discodeit.core.user.usecase.crud.dto;

import java.util.List;

public record UserListResult(
    List<UserResult> userList
) {

}
