package com.sprint.mission.discodeit.adapter.inbound.content.dto;

public record BinaryContentCreateRequestDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
