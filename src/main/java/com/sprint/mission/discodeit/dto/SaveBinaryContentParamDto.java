package com.sprint.mission.discodeit.dto;

public record SaveBinaryContentParamDto(
        String fileName,
        String contentType,
        byte[] fileData
) {

}
