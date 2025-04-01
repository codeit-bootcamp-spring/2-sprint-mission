package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentCreateDto(
        byte[] fileData,
        String filePath,
        String fileName,
        String fileType,
//        String fileExtension,
        long fileSize
) {
    public BinaryContent convertCreateDtoToBinaryContent() {
        return new BinaryContent(fileData, filePath, fileName, fileType, fileSize);
    }
}
