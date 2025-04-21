package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    @NonNull
    ReadStatus save(@NonNull ReadStatus readStatus);

    @NonNull
    Optional<ReadStatus> findById(@NonNull UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);

    void deleteAllByChannelId(UUID channelId);
}
