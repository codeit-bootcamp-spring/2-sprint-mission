package com.sprint.mission.discodeit.core.read.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  //TODO. Query문을 작성하면 쉽게 ReadStatusDTO로 반환할 수 있을거 같긴 함
  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByUser_Id(UUID userId);

  //TODO. Query문을 작성하면 쉽게 ReadStatusDTO로 반환할 수 있을거 같긴 함
  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByChannel_Id(UUID channelId);

  void deleteAllByChannel(Channel channel);
}
