package com.sprint.mission.discodeit.DTO.legacy.ReadStatus;

import lombok.Builder;

import java.util.UUID;
/**
 * 읽기 상태(Read Status) 관리를 위한 DTO 클래스입니다.
 * 특정 사용자가 채널 내에서 마지막으로 읽은 상태를 추적하는 데이터를 포함합니다.
 */
@Builder
public record ReadStatusCRUDDTO(
        UUID readStatusId,
        UUID userId,
        UUID channelId
) {
    /**
     * 새로운 읽기 상태를 생성하기 위한 DTO를 생성합니다.
     *
     * @param userId 읽기 상태를 생성할 사용자의 ID
     * @param channelID 읽기 상태가 속한 채널의 ID
     * @return 읽기 상태 생성 요청을 위한 {@link ReadStatusCRUDDTO} 객체
     */
    public static ReadStatusCRUDDTO create(
            UUID userId,
            UUID channelID
    ) {
        return ReadStatusCRUDDTO.builder()
                .userId(userId)
                .channelId(channelID).build();
    }
    /**
     * 특정 읽기 상태를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param readStatusId 업데이트할 읽기 상태의 ID
     * @return 읽기 상태 업데이트 요청을 위한 {@link ReadStatusCRUDDTO} 객체
     */
    public static ReadStatusCRUDDTO update(UUID readStatusId) {
        return ReadStatusCRUDDTO.builder().readStatusId(readStatusId).build();
    }

}
