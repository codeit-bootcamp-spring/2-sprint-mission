package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
/**
 * 서버 관리를 위한 서비스 인터페이스입니다.
 * 서버의 생성, 조회, 수정, 삭제 및 기타 관련 기능을 제공합니다.
 */
public interface ServerService {
    /**
     * 서버 시스템을 초기화합니다. 관리자의 인증 여부에 따라 수행됩니다.
     *
     * @param adminAuth 관리자로 인증된 경우 {@code true}, 그렇지 않으면 {@code false}
     */
    void reset(boolean adminAuth);
    /**
     * 새로운 서버를 생성하고 고유한 서버 ID(UUID)를 반환합니다.
     *
     * @param serverCRUDDTO 생성할 서버 정보 DTO
     * @return 생성된 서버 객체
     */
    Server create(ServerCRUDDTO serverCRUDDTO);
    /**
     * 기존 서버에 참여하고 참여한 유저 객체를 반환합니다.
     *
     * @param serverCRUDDTO 참여할 서버 정보 DTO
     * @return 참여한 유저 객체
     */
    User join(ServerCRUDDTO serverCRUDDTO);
    /**
     * 기존 서버에 탈퇴하고 참여한 유저를 반환합니다.
     *
     * @param serverCRUDDTO 탈퇴할 서버 정보 DTO
     * @return 탈퇴한 유저 객체
     */
    User quit(ServerCRUDDTO serverCRUDDTO);
    /**
     * 특정 서버 ID를 기반으로 서버 정보를 조회합니다.
     *
     * @param serverId 조회할 서버의 ID
     * @return 조회된 서버 객체
     */
    Server find(String serverId);
    /**
     * 특정 소유자가 보유한 모든 서버 정보를 조회합니다.
     *
     * @param ownerId 서버 소유자의 ID
     * @return 소유자가 가진 서버 목록
     */
    List<Server> findServerAll(String ownerId);

    /**
     * 특정 사용자의 서버 관련 정보를 출력합니다.
     * 출력 방식은 구현 클래스에서 정의됩니다.
     *
     * @param userId 출력할 대상 사용자의 ID
     */
    void print(String userId);

    /**
     * 특정 서버를 삭제합니다.
     *
     * @param serverCRUDDTO 삭제할 서버 정보 DTO
     * @return 삭제 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean delete(ServerCRUDDTO serverCRUDDTO);
    /**
     * 특정 서버의 정보를 업데이트합니다.
     *
     * @param serverId      업데이트할 서버의 ID
     * @param serverCRUDDTO 업데이트할 서버 정보 DTO
     * @return 서버 객체
     */
    Server update(String serverId, ServerCRUDDTO serverCRUDDTO);

}
