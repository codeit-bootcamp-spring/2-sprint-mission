package com.sprint.mission.discodeit.service.dto.binarycontentdto;


public record BinaryContentCreateDto(
        String fileName,
        String contentType,
        byte[] bytes
) {

}
