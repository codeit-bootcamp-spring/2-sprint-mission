package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface SpringDataChannelRepository extends JpaRepository<Channel, UUID> {
}
