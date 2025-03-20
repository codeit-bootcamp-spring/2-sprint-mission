package com.sprint.mission.discodeit.DTO;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] data
) {}
