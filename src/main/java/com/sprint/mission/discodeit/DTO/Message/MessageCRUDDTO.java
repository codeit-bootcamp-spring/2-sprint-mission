package com.sprint.mission.discodeit.DTO.Message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * 메시지(Message) 관리를 위한 DTO 클래스입니다.
 * 메시지의 생성, 삭제 및 업데이트 요청을 처리하는 데이터를 포함합니다.
 */
@Builder
public record MessageCRUDDTO(
        UUID serverId,
        UUID channelId,
        UUID messageId,
        UUID creatorId,
        String text,
        List<BinaryContent> binaryContent
) {

    /**
     * 새로운 메시지를 생성하기 위한 DTO를 생성합니다.
     *
     * @param creatorId 메시지를 생성한 사용자의 ID
     * @param channelId 메시지가 속한 채널의 ID
     * @param text 메시지의 내용
     * @return 메시지 생성 요청을 위한 {@link MessageCRUDDTO} 객체
     */
    public static MessageCRUDDTO create(UUID creatorId,
                                        UUID channelId,
                                        String text) {
        return MessageCRUDDTO.builder()
                .creatorId(creatorId)
                .channelId(channelId)
                .text(text).build();
    }

    /**
     * 새로운 메시지를 생성하기 위한 DTO를 생성합니다.
     *
     * @param creatorId 메시지를 생성한 사용자의 ID
     * @param channelId 메시지가 속한 채널의 ID
     * @param text 메시지의 내용
     * @param binaryContent 메시지에 첨부된 바이너리 콘텐츠 목록 (예: 이미지, 파일 등)
     * @return 메시지 생성 요청을 위한 {@link MessageCRUDDTO} 객체
     */
    public static MessageCRUDDTO createWithFile(UUID creatorId,
                                        UUID channelId,
                                        String text,
                                        List<BinaryContent> binaryContent) {
        return MessageCRUDDTO.builder()
                .creatorId(creatorId)
                .channelId(channelId)
                .text(text)
                .binaryContent(binaryContent).build();
    }
    /**
     * 특정 메시지를 삭제하기 위한 DTO를 생성합니다.
     *
     * @param serverId 메시지가 속한 서버의 ID
     * @param channelId 메시지가 속한 채널의 ID
     * @param messageId 삭제할 메시지의 ID
     * @return 메시지 삭제 요청을 위한 {@link MessageCRUDDTO} 객체
     */
    public static MessageCRUDDTO delete(UUID serverId,
                                        UUID channelId,
                                        UUID messageId) {
        return MessageCRUDDTO.builder()
                .serverId(serverId)
                .channelId(channelId)
                .messageId(messageId).build();
    }
    /**
     * 특정 메시지를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param replaceId 업데이트할 메시지의 ID
     * @param replaceText 변경할 메시지의 내용
     * @return 메시지 업데이트 요청을 위한 {@link MessageCRUDDTO} 객체
     */
    public static MessageCRUDDTO update(UUID replaceId,
                                        String replaceText) {

        return MessageCRUDDTO.builder()
                .messageId(replaceId)
                .text(replaceText).build();
    }

}
