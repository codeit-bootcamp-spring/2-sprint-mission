package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record PublicChannelUpdateRequest(
    String newName,
    String newIntroduction
) {

}
