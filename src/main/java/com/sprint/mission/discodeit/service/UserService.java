package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.*;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 관리를 위한 서비스 인터페이스입니다.
 * 사용자 등록, 조회, 수정, 삭제 및 기타 기능을 제공합니다.
 */
public interface UserService {
    /**
     * 시스템을 초기화합니다. 관리자의 인증 여부에 따라 수행됩니다.
     *
     * @param adminAuth 관리자로 인증된 경우 {@code true}, 그렇지 않으면 {@code false}
     */
    void reset(boolean adminAuth);
    /**
     * 새로운 사용자를 등록하고 고유한 사용자 ID(UUID)를 반환합니다.
     *
     * @param userCRUDDTO 등록할 사용자 정보 DTO
     * @return 생성된 사용자의 고유 ID(UUID)
     */
    User register(UserCRUDDTO userCRUDDTO);
    /**
     * 특정 사용자 ID에 대한 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자 정보를 포함한 {@link UserFindDTO} 객체
     */
    UserFindDTO find(UUID userId);
    /**
     * 모든 사용자 정보를 조회합니다.
     *
     * @return 모든 사용자의 정보를 포함한 리스트
     */
    List<UserFindDTO> findAll();
    /**
     * 현재 시스템의 사용자 정보를 출력합니다.
     * 구체적인 출력 방식은 구현 클래스에서 정의됩니다.
     */
    void print();
    /**
     * 특정 사용자를 삭제합니다.
     *
     * @param userCRUDDTO 삭제할 사용자 정보 DTO
     * @return 삭제 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean delete(UserCRUDDTO userCRUDDTO);
    /**
     * 특정 사용자의 정보를 업데이트합니다.
     *
     * @param userId      업데이트할 사용자의 ID
     * @param userCRUDDTO 업데이트할 사용자 정보 DTO
     * @return 업데이트 성공 여부, 성공하면 {@code User}, 실패하면 {@code null}
     */
    User update(UUID userId, UserCRUDDTO userCRUDDTO);

}
