package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentDto(
        byte[] fileData,
        String filePath,
        String fileName,
        String fileType,
//        String fileExtenstion,
        long fileSize
) {
    public BinaryContent convertToBinaryContent(){
        return new BinaryContent(fileData, filePath, fileName, fileType, fileSize);
    }

    public static BinaryContentDto fromMultipartFile(MultipartFile file, String filePath) throws IOException {
        return new BinaryContentDto(
                file.getBytes(),
                file.getOriginalFilename(),
                filePath,
                file.getContentType(),
//                getFileExtension(file.getOriginalFilename()),
                file.getSize()
        );
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

}
