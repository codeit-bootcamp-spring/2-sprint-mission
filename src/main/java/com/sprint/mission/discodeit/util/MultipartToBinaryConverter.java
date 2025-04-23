package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class MultipartToBinaryConverter {
    public static BinaryContentCreateDto toBinaryContentCreateDto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return new BinaryContentCreateDto(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BinaryContentCreateDto> toBinaryContentCreateDtos(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream()
                .map(MultipartToBinaryConverter::toBinaryContentCreateDto)
                .toList();
    }
}
