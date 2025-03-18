package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.legacy.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * 읽기 상태(Read Status)를 관리하는 서비스 인터페이스입니다.
 * 읽기 상태의 생성, 조회, 수정 및 삭제 기능을 제공합니다.
 */
@Service
public interface ReadStatusService {
    /**
     * 새로운 읽기 상태를 생성하고 고유한 ID(UUID)를 반환합니다.
     *
     * @param readStatusCRUDDTO 생성할 읽기 상태 정보 DTO
     * @return 생성된 읽기 상태의 고유 ID(UUID)
     */
    UUID create(ReadStatusCRUDDTO readStatusCRUDDTO);
    /**
     * 특정 읽기 상태 ID를 기반으로 읽기 상태를 조회합니다.
     *
     * @param readStatusId 조회할 읽기 상태의 ID
     * @return 조회된 읽기 상태 객체
     */
    ReadStatus find(String readStatusId);
    /**
     * 특정 사용자 ID에 해당하는 모든 읽기 상태를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 읽기 상태 목록
     */
    List<ReadStatus> findAllByUserId(String userId);
    /**
     * 특정 읽기 상태 정보를 업데이트합니다.
     *
     * @param readStatusId        업데이트할 읽기 상태의 ID
     * @param readStatusCRUDDTO   업데이트할 읽기 상태 정보 DTO
     */
    void update(String readStatusId, ReadStatusCRUDDTO readStatusCRUDDTO);
    /**
     * 특정 읽기 상태를 삭제합니다.
     *
     * @param readStatusId 삭제할 읽기 상태의 ID
     */
    void delete(String readStatusId);
}
