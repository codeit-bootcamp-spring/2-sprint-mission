package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    @NonNull
    Channel save(@NonNull Channel channel);

    @NonNull
    Optional<Channel> findById(@NonNull UUID channelId);

    @NonNull
    List<Channel> findAll();

    boolean existsById(@NonNull UUID channelId);

    void deleteById(@NonNull UUID channelId);

    boolean findByName(@NonNull String name);
}
