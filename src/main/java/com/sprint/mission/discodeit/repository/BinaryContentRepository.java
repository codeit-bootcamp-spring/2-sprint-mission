
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    // 파일 저장 (Create)
    void save(BinaryContent binaryContent);

    // 모든 파일 조회 (Read)
    List<BinaryContent> findAll();

    // 특정 User가 업로드한 파일 조회
    List<BinaryContent> findByUserId(UUID userId);

    // 특정 Message에 첨부된 파일 조회
    List<BinaryContent> findByMessageId(UUID messageId);

    // 특정 User의 프로필 이미지 조회 (messageId == null)
    Optional<BinaryContent> findProfileImageByUserId(UUID userId);

    // 특정 User의 프로필 이미지 삭제
    void deleteProfileImageByUserId(UUID userId);

    // 특정 Message에 첨부된 파일 삭제
    void deleteByMessageId(UUID messageId);
}