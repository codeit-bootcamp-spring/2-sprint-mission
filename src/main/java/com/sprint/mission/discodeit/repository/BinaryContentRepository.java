package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    Optional<BinaryContent> findById(UUID id); //BinaryContent ID로 조회
    Optional<BinaryContent> findByUserId(UUID userId); //프로필이미지 조회
    List<BinaryContent> findByMessageId(UUID messageId); //파일 목록 조회
    void save(BinaryContent binaryContent);
    void delete(UUID id);
}
