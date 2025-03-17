package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 바이너리 콘텐츠(Binary Content)를 관리하는 서비스 인터페이스입니다.
 * 바이너리 데이터의 생성, 조회 및 삭제 기능을 제공합니다.
 */
@Service
public interface BinaryContentService {
    /**
     * 새로운 바이너리 콘텐츠를 생성하고 반환합니다.
     *
     * @return 생성된 {@link BinaryContent} 객체
     */
    BinaryContent create();
    /**
     * 특정 바이너리 콘텐츠 ID를 기반으로 바이너리 콘텐츠를 조회합니다.
     *
     * @param binaryId 조회할 바이너리 콘텐츠의 ID
     * @return 조회된 {@link BinaryContent} 객체
     */
    BinaryContent find(UUID binaryId);
    /**
     * 여러 개의 바이너리 콘텐츠 ID에 해당하는 바이너리 콘텐츠 목록을 조회합니다.
     *
     * @return 조회된 {@link BinaryContent} 객체 목록
     */
    List<BinaryContent> findAllByIdIn();
    /**
     * 특정 바이너리 콘텐츠를 삭제합니다.
     *
     * @param binaryId 삭제할 바이너리 콘텐츠의 ID
     * @return 삭제 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean delete(UUID binaryId);
}
