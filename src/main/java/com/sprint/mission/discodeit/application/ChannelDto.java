package com.sprint.mission.discodeit.application;

import java.util.List;
import java.util.UUID;

public record ChannelDto(UUID id, String name, List<UserDto> users) {
}
