package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @Query("""
      SELECT rs From ReadStatus rs  
      JOIN FETCH rs.channel c 
      where rs.channel.id = :channelId
      """)
  List<ReadStatus> findByChannelId(UUID channelId);

  @Query("""
      SELECT rs From ReadStatus rs 
      JOIN FETCH rs.user u 
      where rs.user.id = :userId
      """)
  List<ReadStatus> findByUserId(UUID userId);

  @Query("""
      SELECT rs From ReadStatus rs 
      JOIN FETCH rs.user
      where rs.id = :readStatusId
      """)
  Optional<ReadStatus> findFetchUserById(UUID readStatusId);

  boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);

  void deleteAllByChannel_Id(UUID channelId);

}
