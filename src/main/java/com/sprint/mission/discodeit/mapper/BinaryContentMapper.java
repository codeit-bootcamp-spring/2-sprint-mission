package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

    private final BinaryContentStorage binaryContentStorage;

    public BinaryContentDto toDto(BinaryContent binaryContent) {

        try {
            if (binaryContent == null) {
                return null;
            }

            byte[] bytes = binaryContentStorage.get(binaryContent.getId()).readAllBytes();

            return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType(),
                bytes
            );
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] file not found");
        }
    }
}
