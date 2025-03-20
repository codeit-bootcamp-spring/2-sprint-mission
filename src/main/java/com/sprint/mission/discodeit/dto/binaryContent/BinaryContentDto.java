package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentDto(
        byte[] content,
        String contentType
) {}
