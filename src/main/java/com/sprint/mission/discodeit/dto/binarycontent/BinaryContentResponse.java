package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentResponse(UUID id, UUID userId, UUID messageId, byte[] content) {}