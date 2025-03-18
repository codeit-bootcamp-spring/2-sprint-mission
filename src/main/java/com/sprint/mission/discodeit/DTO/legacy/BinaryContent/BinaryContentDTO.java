package com.sprint.mission.discodeit.DTO.legacy.BinaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;
/**
 * 바이너리 콘텐츠(Binary Content) 정보를 제공하는 DTO 클래스입니다.
 * 바이너리 데이터와 관련된 정보를 포함하며, 생성된 시간 및 고유 식별자를 제공합니다.
 */
@Builder
public record BinaryContentDTO(
        String fileName,
        String contentType,
        byte[] bytes,
        BinaryContent binaryContent,
        UUID binaryContentId,
        Instant createdAt
) {

    public static BinaryContentDTO create(String fileName, String contentType, byte[] bytes) {
        return BinaryContentDTO.builder()
                .fileName(fileName)
                .contentType(contentType)
                .bytes(bytes).build();
    }

}
