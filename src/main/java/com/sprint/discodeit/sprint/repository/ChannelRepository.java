package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
