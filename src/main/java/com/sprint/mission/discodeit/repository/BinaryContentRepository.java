package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {


    @NonNull
    BinaryContent save(@NonNull BinaryContent binaryContent);

    @NonNull
    Optional<BinaryContent> findById(@NonNull UUID binaryContentId);

    @NonNull
    List<BinaryContent> findAllByIdIn(@NonNull List<UUID> ids);

    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);
}