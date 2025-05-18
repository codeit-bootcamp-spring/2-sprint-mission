package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  //추후 사용 예정
  List<Channel> findAllByTypeOrIdIn(ChannelType type, List<UUID> ids);
}
