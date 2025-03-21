package com.sprint.mission.discodeit.dto;

public record BinaryContentDto (
        String fileName,
        String contentType,
        byte[] fileData
) { }