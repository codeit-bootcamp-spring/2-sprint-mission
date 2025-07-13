package com.sprint.mission.discodeit.core.channel.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChannelRepository extends JpaRepository<Channel, UUID>,
    CustomChannelRepository {

}
