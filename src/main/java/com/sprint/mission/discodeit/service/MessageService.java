package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.legacy.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 메시지(Message)를 관리하는 서비스 인터페이스입니다.
 * 메시지의 생성, 조회, 수정 및 삭제 기능을 제공합니다.
 */
@Service
public interface MessageService {
    /**
     * 메시지 시스템을 초기화합니다. 관리자의 인증 여부에 따라 수행됩니다.
     *
     * @param adminAuth 관리자로 인증된 경우 {@code true}, 그렇지 않으면 {@code false}
     */
    void reset(boolean adminAuth);
    /**
     * 새로운 메시지를 생성합니다.
     *
     * @param messageCRUDDTO 생성할 메시지 정보 DTO
     * @return 생성된 메시지 객체
     */
    Message create(MessageCRUDDTO messageCRUDDTO);
    /**
     * 특정 메시지 ID를 기반으로 메시지를 조회합니다.
     *
     * @param messageId 조회할 메시지의 ID
     * @return 조회된 메시지 객체
     */
    Message find(String messageId);
    /**
     * 특정 채널 ID에 해당하는 모든 메시지를 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 메시지 목록
     */
    List<Message> findAllByChannelId(String channelId);
    /**
     * 특정 채널의 메시지 정보를 출력합니다.
     * 출력 방식은 구현 클래스에서 정의됩니다.
     *
     * @param channelId 출력할 채널의 ID
     */
    void print(String channelId);
    /**
     * 특정 메시지를 삭제합니다.
     *
     * @param messageCRUDDTO 삭제할 메시지 정보 DTO
     * @return 삭제 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean delete(MessageCRUDDTO messageCRUDDTO);
    /**
     * 특정 메시지를 업데이트합니다.
     *
     * @param messageId      업데이트할 메시지의 ID
     * @param messageCRUDDTO 업데이트할 메시지 정보 DTO
     * @return 업데이트 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean update(String messageId, MessageCRUDDTO messageCRUDDTO);

}
