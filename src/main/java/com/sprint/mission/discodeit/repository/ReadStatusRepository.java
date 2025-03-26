package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    // 사용자와 채널 식별자로 읽음 상태를 조회
    ReadStatus findByUserAndChannel(UUID userId, UUID channelId);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAll();
    void save(ReadStatus readStatus);
    void delete(ReadStatus readStatus);
}
