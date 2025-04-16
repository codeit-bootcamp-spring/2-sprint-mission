package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByType(ChannelType type);

  @Query("""
        SELECT c FROM Channel c
        WHERE c.type = :publicType OR c.id IN :subscribedChannelIds
      """)
  List<Channel> findAccessibleChannels(@Param("publicType") ChannelType publicType,
      @Param("subscribedChannelIds") List<UUID> subscribedChannelIds);
}
