package com.sprint.mission.discodeit.DTO.BinaryContent;

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
        BinaryContent binaryContent,
        UUID binaryContentId,
        Instant createdAt
) {
    /**
     * 새로운 바이너리 콘텐츠 DTO 객체를 생성합니다.
     *
     * @param binaryContent 저장할 바이너리 콘텐츠 객체
     * @return 바이너리 콘텐츠 정보를 포함한 {@link BinaryContentDTO} 객체
     */
    public static BinaryContentDTO create(BinaryContent binaryContent) {
        return BinaryContentDTO.builder()
                .binaryContent(binaryContent)
                .binaryContentId(binaryContent.getBinaryContentId())
                .createdAt(binaryContent.getCreatedAt()).build();
    }
}
