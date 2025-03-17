package com.sprint.mission.discodeit.DTO.Server;

import lombok.Builder;

import java.util.UUID;
/**
 * 서버(Server) 정보를 관리하기 위한 DTO 클래스입니다.
 * 서버의 생성, 참가, 수정 및 삭제 요청을 처리하는 데이터를 포함합니다.
 */
@Builder
public record ServerCRUDDTO(
        UUID serverId,
        UUID userId,
        String name
) {
    /**
     * 특정 서버에 사용자를 참가시키기 위한 DTO를 생성합니다.
     *
     * @param serverId 참가할 서버의 ID
     * @param userId 참가하는 사용자의 ID
     * @return 서버 참가 요청을 위한 {@link ServerCRUDDTO} 객체
     */
    public static ServerCRUDDTO join(UUID serverId,
                                     UUID userId) {
        return ServerCRUDDTO.builder()
                .serverId(serverId)
                .userId(userId).build();
    }

    /**
     * 새 서버를 생성하기 위한 DTO를 생성합니다.
     *
     * @param ownerId 서버 소유자의 ID
     * @param name 생성할 서버의 이름
     * @return 서버 생성 요청을 위한 {@link ServerCRUDDTO} 객체
     */
    public static ServerCRUDDTO create(
            UUID ownerId,
            String name) {
        return ServerCRUDDTO.builder()
                .userId(ownerId)
                .name(name).build();
    }

    /**
     * 특정 서버를 삭제하기 위한 DTO를 생성합니다.
     *
     * @param serverId 삭제할 서버의 ID
     * @param ownerId 서버 소유자의 ID (삭제 권한 확인 목적)
     * @return 서버 삭제 요청을 위한 {@link ServerCRUDDTO} 객체
     */
    public static ServerCRUDDTO delete(
            UUID serverId,
            UUID ownerId
    ) {
        return ServerCRUDDTO.builder()
                .serverId(serverId)
                .userId(ownerId).build();
    }

    /**
     * 특정 서버의 정보를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param replaceServerId 업데이트할 서버의 ID
     * @param replaceOwnerId 업데이트할 서버 소유자의 ID
     * @param replaceName 변경할 서버 이름
     * @return 서버 업데이트 요청을 위한 {@link ServerCRUDDTO} 객체
     */
    public static ServerCRUDDTO update(
            UUID replaceServerId,
            UUID replaceOwnerId,
            String replaceName
    ) {
        return ServerCRUDDTO.builder()
                .serverId(replaceServerId)
                .userId(replaceOwnerId)
                .name(replaceName).build();
    }
}
