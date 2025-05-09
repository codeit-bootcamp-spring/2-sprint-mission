package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;

import com.sprint.mission.discodeit.entity.channel.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("""
      SELECT c 
        FROM Channel c 
        JOIN ReadStatus rs on rs.channel = c 
       WHERE rs.user.id = :userId 
         AND c.type = :privateType
      """)
  List<Channel> findAllPrivateByUserId(
      @Param("userId") UUID userId,
      @Param("privateType") ChannelType privateType
  );

  List<Channel> findAllByType(ChannelType type);
}
