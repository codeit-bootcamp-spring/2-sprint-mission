package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);

    // 추가 메소드
    // 채널의 마지막 메시지 생성 시간을 반환 (없으면 Optional.empty())
    // // 추가: 해당 채널에 속한 모든 메시지 삭제
    Optional<Instant> findLastMessageTimeByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);
}
