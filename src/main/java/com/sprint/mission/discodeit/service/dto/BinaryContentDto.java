package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentDto(
        UUID contentId,
        byte[] fileData,
        String filePath,
        String fileName,
        String fileType,
        long fileSize
) {
    public BinaryContent convertToBinaryContent(){
        return new BinaryContent(fileData, filePath, fileName, fileType, fileSize);
    }
}
