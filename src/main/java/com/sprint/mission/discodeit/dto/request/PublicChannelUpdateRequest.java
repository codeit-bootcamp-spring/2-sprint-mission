package com.sprint.mission.discodeit.dto.request;

import java.util.Optional;

public record PublicChannelUpdateRequest(
    Optional<String> newName,
    Optional<String> newDescription
) {

}
