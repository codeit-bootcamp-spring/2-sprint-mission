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

    @NonNull
    Channel save(@NonNull Channel channel);

    @NonNull
    Optional<Channel> findById(@NonNull UUID channelId);

    @NonNull
    List<Channel> findAll();

    boolean existsById(@NonNull UUID channelId);

    void deleteById(@NonNull UUID channelId);

    Optional<Channel> findByName(@NonNull String name);

    boolean existsByName(@NonNull String name);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM Channel c JOIN c.participants uc WHERE uc.user.id = :userId")
    List<Channel> findChannelsByUserId(@Param("userId") UUID userId);
}
