package com.sprint.mission.discodeit.dto.request;

import java.util.Optional;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id,
        Optional<String> name,
        Optional<String> description
) {}
