package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID contentId,
        byte[] fileData,
        String filePath,
        String fileName,
        String fileType,
        long fileSize
) {
    public static BinaryContentResponseDto convertToResponseDto(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(binaryContent.getId(), binaryContent.getFileData(), binaryContent.getFilePath(), binaryContent.getFileName(), binaryContent.getFileType(), binaryContent.getFileSize());
    }
}
