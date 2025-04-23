package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelJPARepository extends JpaRepository<Channel, UUID> {

    Boolean existsByTypeAndName(ChannelType type, String name);

    List<Channel> findByTypeOrIdIn(ChannelType type, List<UUID> matchingChannelId);
}
