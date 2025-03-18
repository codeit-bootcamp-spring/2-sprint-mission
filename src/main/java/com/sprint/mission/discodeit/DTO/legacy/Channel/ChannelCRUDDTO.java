package com.sprint.mission.discodeit.DTO.legacy.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.util.UUID;

/**
 * 채널(Channel) 관리를 위한 DTO 클래스입니다.
 * 채널의 생성, 참가, 수정 및 삭제 요청을 처리하는 데이터를 포함합니다.
 */
@Builder
public record ChannelCRUDDTO(
        UUID serverId,
        UUID creatorId,
        UUID channelId,
        String name,
        ChannelType type
) {
    /**
     * 새로운 공개 채널을 생성하기 위한 DTO를 생성합니다.
     *
     * @param serverId 채널이 속한 서버의 ID
     * @param creatorId 채널을 생성한 사용자의 ID
     * @param name 생성할 채널의 이름
     * @return 채널 생성 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO create(
            UUID serverId,
            UUID creatorId,
            String name
    ) {
        return ChannelCRUDDTO.builder()
                .serverId(serverId)
                .creatorId(creatorId)
                .name(name)
                .type(ChannelType.PUBLIC).build();
    }
    /**
     * 새로운 비밀 채널을 생성하기 위한 DTO를 생성합니다.
     *
     * @param serverId 채널이 속한 서버의 ID
     * @param creatorId 채널을 생성한 사용자의 ID
     * @param name 생성할 채널의 이름
     * @return 채널 생성 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO createPrivate(
            UUID serverId,
            UUID creatorId,
            String name
    ) {
        return ChannelCRUDDTO.builder()
                .serverId(serverId)
                .creatorId(creatorId)
                .name(name)
                .type(ChannelType.PRIVATE).build();
    }
    /**
     * 특정 사용자가 채널에 참가하기 위한 DTO를 생성합니다.
     *
     * @param userId 참가할 사용자의 ID
     * @param channelId 참가할 채널의 ID
     * @param type 채널 유형
     * @return 채널 참가 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO join(
            UUID userId,
            UUID channelId,
            ChannelType type
    ) {
        return ChannelCRUDDTO.builder()
                .creatorId(userId)
                .channelId(channelId)
                .type(type).build();
    }
    /**
     * 특정 채널을 삭제하기 위한 DTO를 생성합니다.
     *
     * @param serverId 채널이 속한 서버의 ID
     * @param userId 삭제를 요청하는 사용자의 ID
     * @param channelId 삭제할 채널의 ID
     * @return 채널 삭제 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO delete(UUID serverId,
                                        UUID userId,
                                        UUID channelId) {
        return ChannelCRUDDTO.builder()
                .serverId(serverId)
                .creatorId(userId)
                .channelId(channelId).build();
    }
    /**
     * 특정 채널의 키(식별자)를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param userId 업데이트를 요청하는 사용자의 ID
     * @param channelId 업데이트할 채널의 ID
     * @return 채널 키 업데이트 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO updateKey(UUID userId,
                                        UUID channelId) {
        return ChannelCRUDDTO.builder()
                .creatorId(userId)
                .channelId(channelId).build();
    }
    /**
     * 특정 채널의 정보를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param replaceChannelId 업데이트할 채널의 ID
     * @param replaceName 변경할 채널 이름
     * @param replaceType 변경할 채널 유형
     * @return 채널 업데이트 요청을 위한 {@link ChannelCRUDDTO} 객체
     */
    public static ChannelCRUDDTO update(UUID replaceChannelId,
                                        String replaceName,
                                        ChannelType replaceType) {
        return ChannelCRUDDTO.builder()
                .serverId(replaceChannelId)
                .name(replaceName)
                .type(replaceType).build();
    }
}
