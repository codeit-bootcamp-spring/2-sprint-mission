package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {
    private final BinaryContentStorage binaryContentStorage;

    public BinaryContentDto toDto(BinaryContent binaryContent) {
        try (InputStream inputStream = binaryContentStorage.get(binaryContent.getId())) {
            byte[] bytes = inputStream.readAllBytes();
            return BinaryContentDto.builder()
                    .id(binaryContent.getId())
                    .fileName(binaryContent.getFileName())
                    .size(binaryContent.getSize())
                    .contentType(binaryContent.getContentType())
                    .bytes(bytes)
                    .build();
        } catch (IOException e) {
            // 예외 처리 (예: 로그 남기기, 사용자에게 안내 등)
            e.printStackTrace();
            // 필요하다면 null 또는 예외를 다시 던질 수 있음
            return null;
        }
    }
}
