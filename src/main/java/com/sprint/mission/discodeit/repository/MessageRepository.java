package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  // 커서가 없을 때 조회
  @Query("select m from Message m"
      + " where m.channel.id = :channelId"
      + " order by m.createdAt ASC")
  Slice<Message> findAllByChannelIdInitial(@Param("channelId") UUID channelId, Pageable pageable);


  // 커서가 있을 때 조회
  @Query("select m from Message m"
      + " where m.channel.id = :channelId"
      + " and m.createdAt > :cursor" // 첫 요청의 경우 cursor가 없음
      + " order by m.createdAt ASC")
  Slice<Message> findAllByChannelIdAfterCursor(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor, Pageable pageable);
  // Cursor 기반 정렬을 하기 위해선 channelId + createdAt ASC 기반 복합 인덱스 필요

  void deleteByChannelId(UUID channelId);

  @Query("select max(m.createdAt) from Message m where m.channel.id = :channelId")
  Optional<Instant> findLatestMessageTimeByChannelId(@Param("channelId") UUID channelId);
}
