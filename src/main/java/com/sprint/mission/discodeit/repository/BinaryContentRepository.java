
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    // 파일 저장 (Create)
    UUID upsert(BinaryContent binaryContent);
    // 모든 파일 조회 (Read)
    List<BinaryContent> findAll();
    // UUID를 통해 조회
    BinaryContent findById(UUID userId);
    List<BinaryContent> findAllByIdIn(UUID messageId);
    // 특정 Message에 첨부된 파일 삭제
    void deleteByMessageId(UUID messageId);
    void delete(UUID id);
}