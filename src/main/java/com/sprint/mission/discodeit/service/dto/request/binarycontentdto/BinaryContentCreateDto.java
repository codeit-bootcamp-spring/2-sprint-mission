package com.sprint.mission.discodeit.service.dto.request.binarycontentdto;


public record BinaryContentCreateDto(
        String fileName,
        String contentType,
        byte[] bytes
) {

}
