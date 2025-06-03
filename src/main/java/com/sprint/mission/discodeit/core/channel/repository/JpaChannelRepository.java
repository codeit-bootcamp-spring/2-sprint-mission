package com.sprint.mission.discodeit.core.channel.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("SELECT c FROM Channel c WHERE c.type = :type or c.id IN :ids")
  List<Channel> findAllByTypeOrIdIn(ChannelType type, List<UUID> ids);
}
