package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    boolean existsByName(@NonNull String name);

    @Transactional(readOnly = true)
    @Query("SELECT rs.channel FROM ReadStatus rs WHERE rs.user.id = :userId")
    List<Channel> findChannelsByUserId(@Param("userId") UUID userId);

    List<Channel> findAllByType(com.sprint.mission.discodeit.entity.ChannelType type);

    List<Channel> findAllByIdInAndType(List<UUID> ids,
        com.sprint.mission.discodeit.entity.ChannelType type);
}
