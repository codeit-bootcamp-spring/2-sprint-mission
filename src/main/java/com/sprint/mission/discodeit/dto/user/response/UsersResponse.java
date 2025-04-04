package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.user.UserDto;

import java.util.List;

public record UsersResponse(List<UserDto> users) {
}
