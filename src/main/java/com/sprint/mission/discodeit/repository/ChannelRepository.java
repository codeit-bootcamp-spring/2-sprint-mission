package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    void delete(UUID id);
}
