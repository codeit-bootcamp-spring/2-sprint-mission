package com.sprint.mission.discodeit.DTO.BinaryContent;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {}
